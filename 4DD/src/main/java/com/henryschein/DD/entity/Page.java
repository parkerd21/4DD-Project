package com.henryschein.DD.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Page {
    private int id;
    private HashMap<String, Stack<DataElement>> dataElements = new HashMap<>();

    public Page() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
