package com.radiation.exporter.data;

import com.radiation.exporter.dto.DataDto;

public interface TemplateFiller<T> {
    void fillTemplate(T resource, DataDto dto);
}
