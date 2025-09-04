package com.radiation.exporter.data.mapper.medicine;

import com.radiation.exporter.data.mapper.Mapper;
import com.radiation.exporter.dto.medicine.MedicineDataDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GeneralDtoMapper implements Mapper<Object, MedicineDataDto> {
    private final Map<String, Object> data = new HashMap<>();

    @Override
    public Map<String, Object> toMap(MedicineDataDto dto) {
        data.clear();
        data.put("voltage", dto.radTypeDataDto().voltage());
        data.put("workload", dto.radTypeDataDto().workload());
        data.put("radExit", dto.radTypeDataDto().radiationExit());
        return data;
    }
}
