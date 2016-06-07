package com.husk.test.bean;

import java.io.Serializable;

/**
 * Created by google on 16/6/3.
 */
public class User implements Serializable{
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
