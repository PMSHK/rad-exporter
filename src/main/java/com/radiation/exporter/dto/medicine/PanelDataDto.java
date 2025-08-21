package com.radiation.exporter.dto.medicine;

import java.util.List;

public record PanelDataDto(TextFormDataDto textFormDataDto,
                           ProtectionDataDto protectionDataDto,
                           List<MatCharacteristicsDataDto> existedMaterialCharacteristicsDtoList,
                           MatCharacteristicsDataDto recommendedMaterialDto,
                           List<OpeningsDataDto> openingDtoList,
                           SourceDataDto sourceDataDto,
                           String additionalLead) {
}
