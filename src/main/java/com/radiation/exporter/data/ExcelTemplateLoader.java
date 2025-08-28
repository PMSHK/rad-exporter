package com.radiation.exporter.data;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;

public class ExcelTemplateLoader implements TemplateLoader<Workbook> {
    @Override
    public Workbook loadTemplate(String templatePath) throws IOException {
        return WorkbookFactory.create(new File(templatePath));
    }
}
