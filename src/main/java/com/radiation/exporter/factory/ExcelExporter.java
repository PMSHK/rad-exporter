package com.radiation.exporter.factory;

import com.radiation.exporter.data.ExcelPathResolver;
import com.radiation.exporter.data.ExcelTemplateFiller;
import com.radiation.exporter.data.ExcelTemplateLoader;
import com.radiation.exporter.data.ExportRequest;
import com.radiation.exporter.data.ExportResult;
import com.radiation.exporter.data.TemplateFiller;
import com.radiation.exporter.data.TemplateLoader;
import com.radiation.exporter.data.TemplatePathResolver;
import com.radiation.exporter.factory.properties.ExcelExporterProperties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Getter
@Component
@Slf4j
public class ExcelExporter implements Exporter {
    private final ExcelExporterProperties properties;
    private final Set<String> formats;
    private final TemplateFiller<Workbook> templateFiller;
    private final TemplateLoader<Workbook> templateLoader;
    private final TemplatePathResolver templatePathResolver;

    public ExcelExporter(ExcelExporterProperties properties) {
        this.properties = properties;
        this.formats = new HashSet<>(properties.getFormats());
        this.templateFiller = new ExcelTemplateFiller();
        this.templateLoader = new ExcelTemplateLoader();
        this.templatePathResolver = new ExcelPathResolver();
    }

    @Override
    public boolean containFormat(String format) {
        return formats.contains(format);
    }

    @Override
    public ExportResult export(ExportRequest request) {
        Workbook workbook;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            String filePath = templatePathResolver.resolveTemplatePath(request.data());
            workbook = templateLoader.loadTemplate(filePath);
            templateFiller.fillTemplate(workbook, request.data());
            workbook.write(outputStream);
            return new ExportResult(outputStream.toByteArray(),request.format().name(), request.fileName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
