package com.henryschein.DD;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor

@Component
public class DataIdMapper {
    HashMap<String, Integer> XYMappedID = new HashMap<>();
    HashMap<String, Integer> XYZMappedID = new HashMap<>();

    public void addXYkeyValuePair(String XYKey, Integer id) {
        XYMappedID.put(XYKey, id);
    }
    public boolean containsXY(String key) {
        return XYMappedID.containsKey(key);
    }
    public void removeXY(String key) {
        XYMappedID.remove(key);
    }
    public Integer getValueFromXY(String key) {
        return XYMappedID.get(key);
    }


    public void addXYZkeyValuePair(String XYZKey, Integer id) {
        XYZMappedID.put(XYZKey, id);
    }
    public boolean containsXYZ(String key) {
        return XYZMappedID.containsKey(key);
    }
    public void removeXYZ(String key) {
        XYZMappedID.remove(key);
    }
    public Integer getValueFromXYZ(String key) {
        return XYZMappedID.get(key);
    }
}
