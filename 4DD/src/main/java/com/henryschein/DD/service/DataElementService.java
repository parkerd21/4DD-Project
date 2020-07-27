package com.henryschein.DD.service;

import com.henryschein.DD.DataIdMapper;
import com.henryschein.DD.dao.DataElementDAO;
import com.henryschein.DD.dto.DataElementDTO;
import com.henryschein.DD.entity.DataElement;
import com.henryschein.DD.service.cache.DataElementCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class DataElementService {

    private DataElementDAO dataElementDAO;
    private DataElementCacheService dataElementCacheService;
    private DataIdMapper dataIdMapper;

    @Autowired
    public DataElementService(DataElementDAO theDataElementDAO, DataElementCacheService dataElementCacheService, DataIdMapper dataIdMapper) {
        this.dataElementDAO = theDataElementDAO;
        this.dataElementCacheService = dataElementCacheService;
        this.dataIdMapper = dataIdMapper;
    }

    public DataElement getById(Integer dataId) {
        return dataElementCacheService.getById(dataId);
    }

    public DataElement getByCoordinates(DataElementDTO dto) {
        if (Objects.nonNull(dto.getZcoord())) {
            return getByXYZ(dto);
        }
        else {
            return getByXY(dto);
        }
    }

    private DataElement getByXY(DataElementDTO dto) {
        if (dataIdMapper.containsXY(dto.getPageIdXY())) {
            return dataElementCacheService.getById(dataIdMapper.getValueFromXY(dto.getPageIdXY()));
        }
        else {
            DataElement dataElement = dataElementDAO.getByXY(dto.getPageId(), dto.getXcoord(), dto.getYcoord());
            log.info("database: retrieving dataElement " + dataElement.toString());
            dto.setZcoord(dataElement.getZcoord());
            dataElementCacheService.saveToCache(dataElement);
            dataIdMapper.addXYkeyValuePair(dto.getPageIdXY(), dataElement.getDataId());
            dataIdMapper.addXYZkeyValuePair(dto.getPageIdXYZ(), dataElement.getDataId());
            return dataElement;
        }
    }

    private DataElement getByXYZ(DataElementDTO dto) {
        if (dataIdMapper.containsXYZ(dto.getPageIdXYZ())) {
            return dataElementCacheService.getById(dataIdMapper.getValueFromXYZ(dto.getPageIdXYZ()));
        }
        else {
            DataElement dataElement =
                    dataElementDAO.getByXYZ(dto.getPageId(), dto.getXcoord(), dto.getYcoord(), dto.getZcoord());
            log.info("database: retrieving dataElement " + dataElement.toString());
            dataElementCacheService.saveToCache(dataElement);
            dataIdMapper.addXYZkeyValuePair(dto.getPageIdXYZ(), dataElement.getDataId());
            return dataElement;
        }
    }

    public List<DataElement> getAll() {
        return dataElementCacheService.getAll();
    }

    public DataElement createNewDataElement(DataElementDTO dataElementDTO) {
        DataElement dataElement = getByCoordinates(dataElementDTO);
        if (dataElement == null) {
            dataElementDTO.setZcoord(0);
            DataElement newElement = new DataElement(dataElementDTO);
            return dataElementDAO.saveAndFlush(newElement);
        }
        else
            return null;
    }

    public DataElement update(DataElementDTO dataElementDTO) {
        DataElement dataElement = getByCoordinates(dataElementDTO);
        if (dataElement != null) {
            DataElement newElement = new DataElement(dataElementDTO);
            newElement.setZcoord(dataElement.getZcoord() + 1);
            dataIdMapper.removeXY(dataElementDTO.getPageIdXY());
            return dataElementCacheService.update(newElement);
        }
        else
            return null;
    }

    // TODO: Test with postMan
    public List<DataElement> getHistory(DataElementDTO dto) {
        return dataElementCacheService.getHistory(dto);
    }

    @Transactional
    public String deleteByXY(DataElementDTO dto) {
        if (dataElementExists(dto)) {
            dataElementDAO.deleteByXY(dto.getPageId(), dto.getXcoord(), dto.getYcoord());
            return "Deleted dataElement and its history at pageId: " + dto.getPageId() + ", xcoord: " +
                    dto.getXcoord() + ", ycoord: " + dto.getYcoord();
        }
        else
            return "Couldn't find dataElement to delete at pageId: " + dto.getPageId() + ", xcoord: " +
                    dto.getXcoord() + ", ycoord: " + dto.getYcoord();
    }

    public boolean dataElementExists(DataElementDTO dto) {
        DataElement dataElement = dataElementDAO.getByXY(dto.getPageId(), dto.getXcoord(), dto.getYcoord());
        return Objects.nonNull(dataElement);
    }

    public List<DataElement> getByRow(Integer pageId, Integer rowNumber) {
        List<DataElement> rowWithHistory = dataElementDAO.getByRow(pageId, rowNumber);
        return removeHistoryFromResult(rowWithHistory);
    }

    public List<DataElement> getByColumn(Integer pageId, Integer columnNumber) {
        List<DataElement> rowWithHistory = dataElementDAO.getByColumn(pageId, columnNumber);
        return removeHistoryFromResult(rowWithHistory);
    }

    private List<DataElement> removeHistoryFromResult(List<DataElement> rowWithHistory) {
        HashMap<String, DataElement> completedRow = new HashMap<>();
        for (DataElement dataElement: rowWithHistory) {
            String key = dataElement.getPageId().toString() + dataElement.getXcoord() + dataElement.getYcoord();
            if (completedRow.containsKey(key)) {
                if (completedRow.get(key).getZcoord() < dataElement.getZcoord())
                    completedRow.put(key, dataElement);
            }
            else
                completedRow.put(key, dataElement);
        }
        return new ArrayList<>(completedRow.values());
    }

    // TODO:
    public List<DataElement> getByRange(String range) {
        return null;
    }
}
