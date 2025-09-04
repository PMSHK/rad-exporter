package com.radiation.exporter.data.mapper.medicine;

import com.radiation.exporter.data.mapper.Mapper;
import com.radiation.exporter.dto.medicine.MatCharacteristicsDataDto;
import com.radiation.exporter.dto.medicine.OpeningsDataDto;
import com.radiation.exporter.dto.medicine.PanelDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PanelDataDtoMapper implements Mapper<Object, PanelDataDto> {
    private final Map<String, Object> data = new HashMap<>();
    private final MatCharactDataDtoMapper exMatMapper;
    private final OpeningsDtoMapper openingsMapper;

    @Override
    public Map<String, Object> toMap(PanelDataDto dto) {
        data.clear();
        data.put("wallSign", dto.textFormDataDto().wallSign());
        data.put("roomPurpose", dto.textFormDataDto().purposeAdjacentRoom());
        data.put("personCategory", dto.textFormDataDto().personalCategory());
        data.put("dmd", dto.sourceDataDto().dmd());
        data.put("coeff", dto.sourceDataDto().directionCoefficient());
        data.put("distance", dto.sourceDataDto().distance());
        data.put("dose", "Nothing");
        data.put("attenuation", dto.protectionDataDto().weaknessCoefficient());
        data.put("pbEquivalent", dto.protectionDataDto().leadEqv());

        mapExMat(dto.existedMaterialCharacteristicsDtoList());

        data.put("demandLead", dto.recommendedMaterialDto().leadEquivalent());
        data.put("demandMatName", dto.recommendedMaterialDto().info().name() + " " + dto.recommendedMaterialDto().info().density());
        data.put("demandMatThickness", dto.recommendedMaterialDto().thickness());

        mapOpenings(dto.openingDtoList());

        return data;
    }

    private void mapExMat(List<MatCharacteristicsDataDto> materials) {
        for (int i = 0; i < materials.size(); i++) {
            data.putAll(exMatMapper.toMap(materials.get(i), i));
        }
    }

    private void mapOpenings(List<OpeningsDataDto> openings) {
        for (int i = 0; i < openings.size(); i++) {
            data.putAll(openingsMapper.toMap(openings.get(i), i));
        }
    }
}
