package org.csr.core.web.bean;

import java.util.LinkedHashMap;


public class MapBean extends LinkedHashMap<Object, Object> {
    private static final long serialVersionUID = -1099174875279059119L;

    public MapBean() {

    }

    public MapBean(Object... o) {
        put(o);
    }

    public void put(Object... o) {
        for (int i = 1; i < o.length; i += 2) {
            put(String.valueOf(o[i - 1]), o[i]);
        }
    }
    public String getString(Object key) {
        return (String) getString(key, "");
    }
    public String getString(Object key, String defaultValue) {
        String value = (String) get(key);
        return value == null ? defaultValue : value;
    }
}
