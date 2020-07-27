package com.henryschein.DD.service.cache;

import com.henryschein.DD.DataIdMapper;
import com.henryschein.DD.dao.DataElementDAO;
import com.henryschein.DD.dto.DataElementDTO;
import com.henryschein.DD.entity.DataElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class DataElementCacheService {

    private DataElementDAO dataElementDAO;
    private DataIdMapper dataIdMapper;

    public DataElementCacheService(DataElementDAO dataElementDAO, DataIdMapper dataIdMapper) {
        this.dataElementDAO = dataElementDAO;
        this.dataIdMapper = dataIdMapper;
    }

    @Cacheable(value = "dataElements", key = "#dataId")
    public DataElement getById(Integer dataId) {
        DataElement dataElement = dataElementDAO.getById(dataId);
        log.info("getById()");

        logDataElementTransaction(dataId.toString(), dataElement);
        return dataElement;
    }
    @Cacheable(value = "dataElements", key = "#dataElement.getDataId()")
    public DataElement saveToCache(DataElement dataElement) {
        log.info("cache dataElements: added dataElement " + dataElement.toString());
        return dataElement;
    }

    @Cacheable(value = "listsOfDataElements")
    public List<DataElement> getAll() {
        return dataElementDAO.findAll();
    }

    @CacheEvict(value = "listsOfDataElements", allEntries = true)
    public DataElement update(DataElement dataElement) {
        log.info("cache listsOfDataElements: deleted all dataElements");
        return dataElementDAO.saveAndFlush(dataElement);
    }

    @Cacheable(value = "listsOfDataElements")
    public List<DataElement> getHistory(DataElementDTO dto) {
        return dataElementDAO.getHistory(dto.getPageId(), dto.getXcoord(), dto.getYcoord());
    }

    private void logDataElementTransaction(String identifier, DataElement dataElement) {
        if (Objects.nonNull(dataElement)) {
            log.info("database: retrieving dataElement " + dataElement.toString());
            log.info("cache dataElements: added dataElement " + dataElement.toString());
        }
        else
            log.error("dataElement " + identifier + " not found");
    }


}
