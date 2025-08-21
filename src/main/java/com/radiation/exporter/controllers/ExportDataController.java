package com.radiation.exporter.controllers;

import com.radiation.exporter.data.ExportRequest;
import com.radiation.exporter.data.ExportResult;
import com.radiation.exporter.services.ExportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/export")
@RequiredArgsConstructor
@Slf4j
public class ExportDataController {
    private final ExportService exportService;

    @PostMapping()
    public ResponseEntity<byte[]> exportData(@RequestBody ExportRequest request) {
        ExportResult result = exportService.export(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + result.fileName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, result.contentType())
                .body(result.content());
    }
}
