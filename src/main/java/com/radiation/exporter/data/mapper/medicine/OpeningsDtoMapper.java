package com.radiation.exporter.data.mapper.medicine;

import com.radiation.exporter.data.mapper.Mapper;
import com.radiation.exporter.dto.medicine.MatCharacteristicsDataDto;
import com.radiation.exporter.dto.medicine.OpeningsDataDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OpeningsDtoMapper implements Mapper<Object, OpeningsDataDto> {
    private final Map<String, Object> data = new HashMap<>();

    public Map<String,Object> toMap(OpeningsDataDto dto, int index) {
        data.clear();
        data.put("openingName" + index, dto.name());
        data.put("openingLead" + index, dto.thickness());
        return data;
    }

    @Override
    public Map<String, Object> toMap(OpeningsDataDto dto) {
        return toMap(dto, 0); // По умолчанию индекс 0
    }
}
