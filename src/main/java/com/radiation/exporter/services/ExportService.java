package com.radiation.exporter.services;

import com.radiation.exporter.data.ExportRequest;
import com.radiation.exporter.data.ExportResult;
import com.radiation.exporter.data.ExportableData;
import com.radiation.exporter.factory.Exporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExportService implements ExportableData {
    private final List<Exporter> exporters;

    private Exporter getExporter(String format) throws ClassNotFoundException {
        return exporters.stream()
                .filter(exp -> exp.containFormat(format))
                .findFirst()
                .orElseThrow(() -> new ClassNotFoundException("Not found exporter: " + format));
    }

    @Override
    public ExportResult export(ExportRequest request) {

        try {
            Exporter exporter = getExporter(request.format().name());
            return exporter.export(request);
        } catch (ClassNotFoundException e) {
            log.error("Not found exporter for format: {}", request.format().name(), e);
            throw new RuntimeException(e);
        }
    }
}
