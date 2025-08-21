package com.radiation.exporter.data;

public record ExportResult(byte[] content,
                           String contentType,
                           String fileName) {
}
