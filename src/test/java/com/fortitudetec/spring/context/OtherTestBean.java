package com.fortitudetec.spring.context;

public class OtherTestBean {

    private String _name;
    private int _value;
    private SampleTestBean _sampleTestBean;

    public OtherTestBean() {
    }

    public OtherTestBean(String name, int value, SampleTestBean sampleTestBean) {
        _name = name;
        _value = value;
        _sampleTestBean = sampleTestBean;
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

    public SampleTestBean getSampleTestBean() {
        return _sampleTestBean;
    }

    public void setSampleTestBean(SampleTestBean sampleTestBean) {
        _sampleTestBean = sampleTestBean;
    }
}
