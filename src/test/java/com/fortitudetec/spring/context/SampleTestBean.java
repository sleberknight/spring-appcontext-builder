package com.fortitudetec.spring.context;

public class SampleTestBean {

    private String _name;
    private int _value;

    public SampleTestBean() {
    }

    public SampleTestBean(String name, int value) {
        _name = name;
        _value = value;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public int getValue() {
        return _value;
    }

    public void setValue(int value) {
        _value = value;
    }
}
