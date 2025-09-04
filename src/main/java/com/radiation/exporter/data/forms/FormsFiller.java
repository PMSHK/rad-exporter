package com.radiation.exporter.data.forms;

public interface  FormsFiller <F,D> {
    String type();
    void fill(F form, D data);
}
