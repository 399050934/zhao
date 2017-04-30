package com.example.brave.curriculum;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by brave on 2017/4/14.
 */

public class SerializableMap implements Serializable {
    private HashMap<String, String> map;

    public HashMap<String, String> getMap() {
        return map;
    }

    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }
}
