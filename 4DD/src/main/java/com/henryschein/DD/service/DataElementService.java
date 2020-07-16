package com.henryschein.DD.service;

import com.henryschein.DD.dao.DataElementDAO;
import com.henryschein.DD.dto.DataElementDTO;
import com.henryschein.DD.entity.DataElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class DataElementService {

    private DataElementDAO dataElementDAO;

    @Autowired
    public DataElementService(DataElementDAO theDataElementDAO) {
        dataElementDAO = theDataElementDAO;
    }

    public DataElement getByXYZ(DataElementDTO dto) {
        return dataElementDAO.getByXYZ(dto.getPageId(), dto.getXcoord(), dto.getYcoord(), dto.getZcoord());
    }

    public DataElement getByXY(DataElementDTO dto) {
        return dataElementDAO.getByXY(dto.getPageId(), dto.getXcoord(), dto.getYcoord());
    }

    public List<DataElement> getAll() {
        return dataElementDAO.findAll();
    }

    public DataElement createNewDataElement(DataElementDTO dataElementDTO) {
        DataElement initialElement = getByXY(dataElementDTO);
        if (initialElement == null) {
            dataElementDTO.setZcoord(1);
            DataElement newElement = new DataElement(dataElementDTO);
            return dataElementDAO.saveAndFlush(newElement);
        }
        else
            return null;
    }

    public DataElement update(DataElementDTO dataElementDTO) {
        DataElement initialElement = getByXY(dataElementDTO);
        if (initialElement != null) {
            DataElement newElement = new DataElement(dataElementDTO);
            newElement.setZcoord(initialElement.getZcoord() + 1);
            return dataElementDAO.saveAndFlush(newElement);
        }
        else
            return null;
    }

    public List<DataElement> getHistory(DataElementDTO dto) {
        return dataElementDAO.getHistory(dto.getPageId(), dto.getXcoord(), dto.getYcoord());
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

    public List<DataElement> getByRow(Long pageId, Integer rowNumber) {
        List<DataElement> rowWithHistory = dataElementDAO.getByRow(pageId, rowNumber);
        return removeHistoryFromResult(rowWithHistory);
    }

    public List<DataElement> getByColumn(Long pageId, Integer columnNumber) {
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
