package com.nextsol.khangbb.util;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;

@UtilityClass
public class DataUtil {

    public boolean isNullOrEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    public boolean isNullObject(Object obj) {
        return (obj == null);
    }
}
