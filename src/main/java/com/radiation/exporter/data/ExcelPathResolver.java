package com.radiation.exporter.data;

import com.radiation.exporter.dto.DataDto;

import java.io.IOException;

public class ExcelPathResolver implements TemplatePathResolver {
    @Override
    public String resolveTemplatePath(DataDto dto) throws IOException {
        return "templates/excel/" + dto.getTemplateName() + ".xlsx";
    }
}
