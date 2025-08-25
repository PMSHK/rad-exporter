package com.radiation.exporter.factory;

import com.radiation.exporter.data.ExportRequest;
import com.radiation.exporter.data.ExportResult;
import com.radiation.exporter.factory.properties.ExcelExporterProperties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Getter
@Component
@Slf4j
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
        Workbook workbook;
        ExportResult result;

        if (request.format().name().equals("xls")) {
            workbook = new HSSFWorkbook();
        } else if (request.format().name().equals("xlsx")) {
            workbook = new XSSFWorkbook();
        } else {
            log.warn("unsupported format: {}",request.format());
            throw new IllegalArgumentException("Unsupported format: " + request.format());
        }

        try (workbook) {
            workbook.createSheet("Protection");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
