package com.radiation.exporter.data;

import com.radiation.exporter.dto.DataDto;

import java.io.IOException;

public interface TemplatePathResolver {
    String resolveTemplatePath(DataDto dto) throws IOException;
}
