package com.henryschein.DD;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.henryschein.DD.dto.DataElementDTO;
import com.henryschein.DD.entity.Book;
import com.henryschein.DD.entity.DataElement;
import com.henryschein.DD.entity.Page;
import com.henryschein.DD.service.DataElementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class TheCacheManager {
    private final Cache<String, Map<Integer, DataElement>> dataElementCache = Caffeine.newBuilder().build();
    private final Cache<String, List<DataElement>> dataElementListCache = Caffeine.newBuilder().build();

    public DataElement retrieveDataElement(DataElementDTO dto) {
        Map<Integer, DataElement> dataElementMapXY = dataElementCache.getIfPresent(dto.getPageIdXY());
        if (dataElementMapXY == null) {
            return null;
        }
        DataElement dataElement;
        if (dto.getZcoord() == null) {
            Integer maxZvalue = Collections.max(dataElementMapXY.keySet());
            dataElement = dataElementMapXY.get(maxZvalue);
        } else {
            dataElement = dataElementMapXY.getOrDefault(dto.getZcoord(), null); //
        }
        if (dataElement != null) {
            log.info("dataElementCache: retrieved dataElement " + dataElement.toString());
        } else {
            log.info("dataElementCache: could not find dataElement " + dataElement.toString());
        }
        return dataElement;
    }

    public List<DataElement> retrieveDataElementList(String key) {
        List<DataElement> dataElementList = dataElementListCache.getIfPresent(key);
        if (dataElementList != null && !dataElementList.isEmpty()) {
            log.info("dataElementListCache: retrieved list " + dataElementList.toString());
        }
        return dataElementList;
    }

    public void invalidateDataElementListCache() {
        dataElementListCache.invalidateAll();
        log.info("dataElementListCache: deleted all dataElements");
    }

    public void invalidateDataElementCache(DataElementDTO dto) {
        dataElementCache.invalidate(dto.getPageIdXY());
        log.info("dataElementCache: deleted dataElements at " + DataElementService.prettyPrintCoordsAndPageId(dto));
    }

    public void updateDataElementCacheRefresh(DataElement newElement, String key) {
        Map<Integer, DataElement> map = dataElementCache.getIfPresent(key);
        if (Objects.nonNull(map)) {
            map.put(newElement.getZcoord(), newElement);
            log.info("dataElementCache: saved  " + newElement.toString());
            invalidateDataElementListCache();
        }
    }

    public void saveDataElementListToListCache(String key, List<DataElement> dataElementList) {
        dataElementListCache.put(key, dataElementList);
        log.info("dataElementListCache: saved list " + dataElementList.toString());
    }

    public void saveDataElementListToDataElementCache(List<DataElement> dataElements) {
        for (DataElement dataElement : dataElements) {
            saveDataElementToCache(dataElement);
        }
    }

    public void saveDataElementToCache(DataElement dataElement) {
        Map<Integer, DataElement> map = dataElementCache.getIfPresent(dataElement.getPageIdXY());
        if (Objects.isNull(map)) {
            map = new HashMap<>();
        }
        if (!map.containsKey(dataElement.getZcoord())) {
            map.put(dataElement.getZcoord(), dataElement);
            dataElementCache.put(dataElement.getPageIdXY(), map);
            log.info("dataElementCache: saved - " + dataElement.toString());
        }
    }

    public void invalidateDataElementCacheByPage(Integer pageId) {
        Set<String> listOfKeys = dataElementCache.asMap().keySet();
        for (String key : listOfKeys) {
            if (key.startsWith(pageId.toString())) {
                String mapAsString = dataElementCache.getIfPresent(key).toString();
                dataElementCache.invalidate(key);
                log.info("dataElementCache: deleted map " + mapAsString);
            }
        }
    }

    public void invalidateDataElementCacheByBook(Book book) {
        for (Page page : book.getPages()) {
            invalidateDataElementCacheByPage(page.getPageId());
        }
    }
}
