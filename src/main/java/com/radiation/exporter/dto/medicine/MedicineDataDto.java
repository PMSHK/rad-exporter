package com.radiation.exporter.dto.medicine;

import com.radiation.exporter.dto.DataDto;

import java.util.List;

public record MedicineDataDto(RadTypeDataDto radTypeDataDto, List<PanelDataDto> panelDataDtoList) implements DataDto {
}
