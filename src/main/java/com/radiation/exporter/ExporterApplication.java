package com.radiation.exporter;

import com.radiation.exporter.factory.properties.ExcelExporterProperties;
import com.radiation.exporter.factory.properties.PdfExporterProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableConfigurationProperties({ExcelExporterProperties.class, PdfExporterProperties.class})
public class ExporterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExporterApplication.class, args);
    }

}
