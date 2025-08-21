package com.radiation.exporter.dto.medicine;

public record RadTypeDataDto(String name,
                             Long voltage,
                             Double radiationExit,
                             Long workload) {
    public RadTypeDataDto {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be null or blank");
        }
        if (voltage == null || voltage <= 0) {
            throw new IllegalArgumentException("Voltage must be positive");
        }
        if (radiationExit == null || radiationExit <= 0) {
            throw new IllegalArgumentException("RadiationExit must be positive");
        }
        if (workload == null || workload <= 0) {
            throw new IllegalArgumentException("Workload must be positive");
        }
    }
}
