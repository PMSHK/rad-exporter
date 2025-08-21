package com.radiation.exporter.data;

import com.radiation.exporter.dto.DataDto;

public record ExportRequest(
        ExportFormat format,
        DataDto data,
        String fileName
) {
}
