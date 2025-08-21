package com.radiation.exporter.dto.medicine;

public record MatCharacteristicsDataDto(MaterialInfoDataDto info,
                                        Double thickness,
                                        Double leadEquivalent) {
}
