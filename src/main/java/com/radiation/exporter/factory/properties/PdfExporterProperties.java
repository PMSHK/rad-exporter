package com.radiation.exporter.factory.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "data.exporters.pdf")
@Getter
public class PdfExporterProperties {
    private List<String> formats;
}
