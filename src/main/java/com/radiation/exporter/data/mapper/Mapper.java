package com.radiation.exporter.data.mapper;

import java.util.Map;

public interface Mapper <T,O> {
    Map<String, T> toMap(O o);
}
