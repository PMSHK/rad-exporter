package com.radiation.exporter.data.forms;

import com.radiation.exporter.dto.medicine.MedicineDataDto;
import com.radiation.exporter.dto.medicine.PanelDataDto;
import com.radiation.exporter.services.ExcelTemplateProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ExcelMedicineFiller implements FormsFiller<Workbook, MedicineDataDto> {
    private final Map<String, Object> data = new HashMap<>();
    private final ExcelTemplateProcessor processor = new ExcelTemplateProcessor();

    @Override
    public String type() {
        return "medicine";
    }

    @Override
    public void fill(Workbook workbook, MedicineDataDto dataDto) {
        init(dataDto);
        processor.replacePlaceholders(workbook, data);
        List< PanelDataDto> panels = dataDto.panelDataDtoList();
        if (panels.size() > 1) {
            for (int i = 0; i < panels.size() + 1; i++) {
                Map<String, Object> map = toMap(panels.get(i));
                processor.copyNamedRangeWithContent(workbook,"panel","panel" + i,0,i,
                        map);
            }
        } else if(dataDto.panelDataDtoList().size() == 1) {
//            processor.replacePlaceholder(workbook,);
        }

    }

    private void init(MedicineDataDto dataDto) {
        data.clear();
        data.put("voltage", dataDto.radTypeDataDto().voltage());
        data.put("workload", dataDto.radTypeDataDto().workload());
        data.put("radExit", dataDto.radTypeDataDto().radiationExit());
    }

    public Map<String, Object> toMap(PanelDataDto dto) {
        Map<String, Object> map = new HashMap<>();
        map.put("wallSign", dto.textFormDataDto().wallSign());
        map.put("roomPurpose", dto.textFormDataDto().purposeAdjacentRoom());
        map.put("personCategory", dto.textFormDataDto().personalCategory());
        map.put("dmd", dto.sourceDataDto().dmd());
        map.put("coeff", dto.sourceDataDto().directionCoefficient());
        map.put("distance", dto.sourceDataDto().distance());
//        map.put("dose", );
        map.put("attenuation", dto.protectionDataDto().weaknessCoefficient());
        map.put("pbEquivalent", dto.protectionDataDto().leadEqv());


        map.put("demandLead", dto.recommendedMaterialDto().leadEquivalent());
        map.put("demandMatName", dto.recommendedMaterialDto().info().name() + " " + dto.recommendedMaterialDto().info().density());
        map.put("demandMatThickness", dto.recommendedMaterialDto().thickness());



        return map;
    }
}
