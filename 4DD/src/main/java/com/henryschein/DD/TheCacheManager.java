package com.henryschein.DD;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.henryschein.DD.entity.DataElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@Setter
@AllArgsConstructor


@Component
public class TheCacheManager {

    private final Cache<String, Map<Integer, DataElement>> dataElementCache = Caffeine.newBuilder().build();
    private final Cache<String, List<DataElement>> dataElementListCache = Caffeine.newBuilder().build();
    public static final String DATA_ELEMENT_ALL_KEY = "getAllKey";


}
