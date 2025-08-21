package com.radiation.exporter.factory.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "data.exporters.excel")
@Getter
public class ExcelExporterProperties {
    private List<String> formats;
}
