package com.radiation.exporter.data;

public interface ExportableData {
    ExportResult export(ExportRequest dto);
}
