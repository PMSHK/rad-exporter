package com.radiation.exporter.factory;

import com.radiation.exporter.data.ExportRequest;
import com.radiation.exporter.data.ExportResult;
import com.radiation.exporter.factory.properties.PdfExporterProperties;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Getter
@Component
public class PdfExporter implements Exporter {
    private final PdfExporterProperties properties;
    private final Set<String> formats;

    public PdfExporter(PdfExporterProperties properties) {
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
