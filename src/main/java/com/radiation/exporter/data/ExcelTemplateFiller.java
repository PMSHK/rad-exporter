package com.radiation.exporter.data;

import com.radiation.exporter.data.forms.FormsFiller;
import com.radiation.exporter.dto.DataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExcelTemplateFiller implements TemplateFiller<Workbook> {

    private final List<FormsFiller<Workbook, DataDto>> formsFiller;

    private FormsFiller<Workbook, DataDto> getFormFiller(String type){
        return formsFiller.stream().filter(f->f.type().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Forms filler not found for type: " + type));
    }

    @Override
    public void fillTemplate(Workbook workbook, DataDto data) {
        FormsFiller<Workbook, DataDto> filler = getFormFiller(data.getTemplateName());
        if (filler!=null){
            filler.fill(workbook, data);
        }   else {
            throw new RuntimeException("Forms filler not found for template: " + data.getTemplateName());
        }

    }
}
