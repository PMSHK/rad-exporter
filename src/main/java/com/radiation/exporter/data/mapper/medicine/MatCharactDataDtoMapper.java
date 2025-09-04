package com.radiation.exporter.data.mapper.medicine;

import com.radiation.exporter.data.mapper.Mapper;
import com.radiation.exporter.dto.medicine.MatCharacteristicsDataDto;
import com.radiation.exporter.dto.medicine.MaterialInfoDataDto;
import com.radiation.exporter.dto.medicine.PanelDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MatCharactDataDtoMapper implements Mapper<Object, MatCharacteristicsDataDto> {
    private final Map<String, Object> data = new HashMap<>();

    public Map<String,Object> toMap(MatCharacteristicsDataDto dto, int index) {
        data.clear();
        data.put("exMatName" + index, dto.info().name() + " " + dto.info().density());
        data.put("exMatThick" + index, dto.thickness());
        data.put("exMatLead" + index, dto.leadEquivalent());
        return data;
    }

    @Override
    public Map<String, Object> toMap(MatCharacteristicsDataDto dto) {
        return toMap(dto, 0); // По умолчанию индекс 0
    }
}
