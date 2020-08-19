package com.henryschein.DD;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.henryschein.DD.entity.DataElement;
import com.henryschein.DD.entity.Page;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Getter
@Setter
@AllArgsConstructor


@Component
public class TheCacheManager {

    private final Cache<String, Map<Integer, DataElement>> dataElementCache = Caffeine.newBuilder().build();
    private final Cache<String, List<DataElement>> dataElementListCache = Caffeine.newBuilder().build();
    private final Cache<Integer, Page> pageCache = Caffeine.newBuilder().build();
    private final Cache<String, List<Page>> pageListCache = Caffeine.newBuilder().build();

    public static final String DATA_ELEMENT_ALL_KEY = "getAllKey";

    public void saveListOfDataElementsToCache(List<DataElement> dataElements) {
        for (DataElement dataElement : dataElements) {
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
    }

    public void saveListOfPagesToCache(List<Page> pagesList) {

    }
}
