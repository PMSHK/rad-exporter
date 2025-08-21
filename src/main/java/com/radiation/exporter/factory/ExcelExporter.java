package com.radiation.exporter.factory;

import com.radiation.exporter.data.ExportRequest;
import com.radiation.exporter.data.ExportResult;
import com.radiation.exporter.factory.properties.ExcelExporterProperties;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Getter
@Component
public class ExcelExporter implements Exporter {
    private final ExcelExporterProperties properties;
    private final Set<String> formats;

    public ExcelExporter(ExcelExporterProperties properties) {
        this.properties = properties;
        this.formats = new HashSet<>(properties.getFormats());
    }

    @Override
    public boolean containFormat(String format) {
        return formats.contains(format);
    }

    @Override
    public ExportResult export(ExportRequest request) {

    }
}
