package com.henryschein.DD.service;

import com.henryschein.DD.TheCacheManager;
import com.henryschein.DD.dao.DataElementDAO;
import com.henryschein.DD.dto.DataElementDTO;
import com.henryschein.DD.entity.DataElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DataElementService {

    private final DataElementDAO dataElementDAO;
    private final PageService pageService;
    private final TheCacheManager cacheManager;


    public DataElementService(DataElementDAO theDataElementDAO, PageService pageService, TheCacheManager cacheManager) {
        this.dataElementDAO = theDataElementDAO;
        this.pageService = pageService;
        this.cacheManager = cacheManager;
    }

    public DataElement getByCoordinates(DataElementDTO dto) {
        Map<Integer, DataElement> map = getDataElementMap(dto);
        if (Objects.isNull(map))
            return null;
        if (Objects.nonNull(dto.getZcoord()))
            return getByXYZ(map, dto.getZcoord());
        else
            return getByXY(map);
    }

    private DataElement getByXY(Map<Integer, DataElement> map) {
        Integer maxZvalue = Collections.max(map.keySet());
        return map.get(maxZvalue);
    }

    private DataElement getByXYZ(Map<Integer, DataElement> map, Integer zCoord) {
        return map.get(zCoord);
    }

    private Map<Integer, DataElement> getDataElementMap(DataElementDTO dto) {
        Map<Integer, DataElement> map = cacheManager.getDataElementCache().getIfPresent(dto.getPageIdXY());
        if (Objects.isNull(map)) {
            List<DataElement> history = dataElementDAO.getHistory(dto.getPageId(), dto.getXcoord(), dto.getYcoord());
            if (!history.isEmpty()) {
                log.info("database: retrieving - list " + dto.getPageId() + " [" + dto.getXcoord() + "," + dto.getYcoord() + "] " + history.toString());
                map = history.stream().collect(Collectors.toMap(DataElement::getZcoord, dataElement -> dataElement));
                cacheManager.getDataElementCache().put(dto.getPageIdXY(), map);
                log.info("dataElementCache: saved - map " + dto.getPageId() + " [" + dto.getXcoord() + "," + dto.getYcoord() + "] " + history.toString());
            }
        } else {
            log.info("dataElementCache: retrieving map - " + dto.getPageId() + " [" + dto.getXcoord() + "," + dto.getYcoord() + "] " + map.toString());
        }
        return map;
    }

    public List<DataElement> getAll() {
        List<DataElement> dataElements =
                cacheManager.getDataElementListCache().getIfPresent(TheCacheManager.DATA_ELEMENT_ALL_KEY);
        if (dataElements == null || dataElements.isEmpty()) {
            dataElements = dataElementDAO.findAll();
            log.info("database: retrieving all dataElements");
            cacheManager.getDataElementListCache().put(TheCacheManager.DATA_ELEMENT_ALL_KEY, dataElements);
            log.info("dataElementListCache: saved all dataElements");
            saveListOfDataElementsToCache(dataElements);
        } else {
            log.info("dataElementListCache: retrieving all dataElements");
        }
        return dataElements;
    }

    public DataElement createNewDataElement(DataElementDTO dataElementDTO) {
        DataElement dataElement = getByCoordinates(dataElementDTO);
        if (dataElement == null) {
            dataElementDTO.setZcoord(1);
            DataElement newElement = new DataElement(dataElementDTO);
            cacheManager.getDataElementListCache().invalidateAll();
            newElement = dataElementDAO.saveAndFlush(newElement);
            log.info("dataBase: saved - " + newElement.toString());
            return newElement;
        } else
            return null;
    }

    public DataElement updateDataElement(DataElementDTO dto) {
        DataElement dataElement = getByCoordinates(dto);
        if (dataElement != null) {
            DataElement newElement = new DataElement(dto);
            newElement.setZcoord(dataElement.getZcoord() + 1);
            newElement = dataElementDAO.saveAndFlush(newElement);
            log.info("dataBase: saved - " + newElement.toString());
            Map<Integer, DataElement> map = cacheManager.getDataElementCache().getIfPresent(dto.getPageIdXY());
            assert map != null;
            map.put(newElement.getZcoord(), newElement);
            log.info("dataElementCache: saved -  " + newElement.toString());
            cacheManager.getDataElementListCache().invalidateAll();
            log.info("dataElementListCache: deleted - all contents");
            return newElement;
        }
        else
            return null;
    }

    public List<DataElement> getHistory(DataElementDTO dto) {
        String key = "getByHistory" + dto.getPageIdXY();
        List<DataElement> dataElementList = cacheManager.getDataElementListCache().getIfPresent(key);
        if (dataElementList == null || dataElementList.isEmpty()) {
            dataElementList = dataElementDAO.getHistory(dto.getPageId(), dto.getXcoord(), dto.getYcoord());
            dataElementList = savePrepareAndLogList(key, dataElementList);
        }
        return dataElementList;
    }

    @Transactional
    public void deleteByXY(DataElementDTO dto) {
        DataElement dataElement = getByCoordinates(dto);
        if (dataElement != null) {
            dataElementDAO.deleteByXY(dto.getPageId(), dto.getXcoord(), dto.getYcoord());
            log.info("dataBase: deleted - " + dataElement.toString());
            Map<Integer, DataElement> map = cacheManager.getDataElementCache().getIfPresent(dto.getPageIdXY());
            assert map != null;
            map.remove(dataElement.getZcoord());
            log.info("dataElementCache: deleted - " + dataElement.toString());
            cacheManager.getDataElementListCache().invalidateAll();
            log.info("dataElementListCache: deleted - all contents");
        }
    }

    public List<DataElement> getByRow(Integer pageId, Integer rowNumber) {
        String key = "getByRow" + pageId + rowNumber;
        List<DataElement> dataElementList = cacheManager.getDataElementListCache().getIfPresent(key);
        if (dataElementList == null || dataElementList.isEmpty()) {
            dataElementList = dataElementDAO.getByRow(pageId, rowNumber);
            dataElementList = savePrepareAndLogList(key, dataElementList);
        }
        return dataElementList;
    }

    public List<DataElement> getByColumn(Integer pageId, Integer columnNumber) {
        String key = "getByColumn" + pageId + columnNumber;
        List<DataElement> dataElementList = cacheManager.getDataElementListCache().getIfPresent(key);
        if (dataElementList == null || dataElementList.isEmpty()) {
            dataElementList = dataElementDAO.getByColumn(pageId, columnNumber);
            dataElementList = savePrepareAndLogList(key, dataElementList);
        }
        return dataElementList;
    }

    private void saveListOfDataElementsToCache(List<DataElement> dataElements) {
        for (DataElement dataElement : dataElements) {
            Map<Integer, DataElement> map = cacheManager.getDataElementCache().getIfPresent(dataElement.getPageIdXY());
            if (Objects.isNull(map)) {
                map = new HashMap<>();
            }
            map.put(dataElement.getZcoord(), dataElement);
            cacheManager.getDataElementCache().put(dataElement.getPageIdXY(), map);
            log.info("dataElementCache: saved - " + dataElement.toString());
        }
    }

    private List<DataElement> savePrepareAndLogList(String key, List<DataElement> dataElementList) {
        if (!dataElementList.isEmpty()) {
            saveListOfDataElementsToCache(dataElementList);
            dataElementList = removeHistoryFromResult(dataElementList);
            cacheManager.getDataElementListCache().put(key, dataElementList);
            log.info("dataElementListCache: saved - list " + dataElementList.toString());
        }
        return dataElementList;
    }

    private List<DataElement> removeHistoryFromResult(List<DataElement> rowWithHistory) {
        Map<String, DataElement> completedRow = new HashMap<>();
        for (DataElement dataElement : rowWithHistory) {
            String key = dataElement.getPageIdXY();
            if (completedRow.containsKey(key)) {
                if (completedRow.get(key).getZcoord() < dataElement.getZcoord())
                    completedRow.put(key, dataElement);
            } else
                completedRow.put(key, dataElement);
        }
        return new ArrayList<>(completedRow.values());
    }

    // TODO:
    public List<DataElement> getByRange(String range) {
        return null;
    }
}
