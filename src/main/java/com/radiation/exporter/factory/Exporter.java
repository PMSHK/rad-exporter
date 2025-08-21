package com.radiation.exporter.factory;

import com.radiation.exporter.data.ExportRequest;
import com.radiation.exporter.data.ExportResult;

public interface Exporter {
    boolean containFormat(String format);

    ExportResult export(ExportRequest request);
}
