package com.radiation.exporter.data.mapper.medicine;

import com.radiation.exporter.dto.medicine.MatCharacteristicsDataDto;
import com.radiation.exporter.dto.medicine.MaterialInfoDataDto;
import com.radiation.exporter.dto.medicine.OpeningsDataDto;
import com.radiation.exporter.dto.medicine.PanelDataDto;
import com.radiation.exporter.dto.medicine.ProtectionDataDto;
import com.radiation.exporter.dto.medicine.SourceDataDto;
import com.radiation.exporter.dto.medicine.TextFormDataDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PanelDataDtoMapperTest {

    @Spy
    private MatCharactDataDtoMapper exMapper;
    @Spy
    private OpeningsDtoMapper openingsMapper;
    @Mock
    private Map<String, Object> data;
    @InjectMocks
    private PanelDataDtoMapper mapper;

    @Test
    public void checkMapping() {
        TextFormDataDto textFormDataDto = new TextFormDataDto("A", "Operational", "A");
        ProtectionDataDto protectionDataDto = new ProtectionDataDto(2756.0, 15.0);
        MatCharacteristicsDataDto firstMat = new MatCharacteristicsDataDto(new MaterialInfoDataDto("Steel", 7.5F), 12D, 4D);
        MatCharacteristicsDataDto secondMat = new MatCharacteristicsDataDto(new MaterialInfoDataDto("Concrete", 2.3F), 250D, 7D);
        MatCharacteristicsDataDto recommendedMat = new MatCharacteristicsDataDto(new MaterialInfoDataDto("Lead", 11.35F), 1D, 1D);

        SourceDataDto sourceDataDto = new SourceDataDto(12D, 1D, 2D);
        String additionalLead = "Does not demanded";
        OpeningsDataDto door = new OpeningsDataDto("Door", String.valueOf(0.75D));
        OpeningsDataDto window = new OpeningsDataDto("Window", String.valueOf(2.5D));
        OpeningsDataDto shutter = new OpeningsDataDto("Shutter", String.valueOf(1.25D));

        List<MatCharacteristicsDataDto> exMaterials = List.of(firstMat, secondMat);
        List<OpeningsDataDto> openings = List.of(door,window, shutter);

        PanelDataDto dto = new PanelDataDto(textFormDataDto, protectionDataDto, exMaterials,
                recommendedMat, openings, sourceDataDto, additionalLead);


        Map<String, Object> result = mapper.toMap(dto);
        System.out.println("dto: " + dto);
        assertEquals(textFormDataDto.wallSign(),result.get("wallSign"));
        assertEquals(textFormDataDto.purposeAdjacentRoom(),result.get("roomPurpose"));
        assertEquals(textFormDataDto.personalCategory(),result.get("personCategory"));
        assertEquals(sourceDataDto.dmd(),result.get("dmd"));
        assertEquals(sourceDataDto.directionCoefficient(), result.get("coeff"));
        assertEquals(sourceDataDto.distance(),result.get("distance"));
        assertEquals("Nothing", result.get("dose"));
        assertEquals(protectionDataDto.weaknessCoefficient(),result.get("attenuation"));
        assertEquals(protectionDataDto.leadEqv(),result.get("pbEquivalent"));
        assertEquals(recommendedMat.info().name() + " " + recommendedMat.info().density(),result.get("demandMatName"));
        assertEquals(recommendedMat.leadEquivalent(),result.get("demandLead"));
        assertEquals(recommendedMat.thickness(),result.get("demandMatThickness"));
        assertEquals(exMaterials.get(0).info().name()+ " " + exMaterials.get(0).info().density(),result.get("exMatName0"));
        assertEquals(exMaterials.get(1).info().name()+ " " + exMaterials.get(1).info().density(),result.get("exMatName1"));
        assertEquals(exMaterials.get(0).thickness(),result.get("exMatThick0"));
        assertEquals(exMaterials.get(1).thickness(),result.get("exMatThick1"));
        assertEquals(exMaterials.get(0).leadEquivalent(),result.get("exMatLead0"));
        assertEquals(exMaterials.get(1).leadEquivalent(),result.get("exMatLead1"));
        assertEquals(openings.get(0).name(),result.get("openingName0"));
        assertEquals(openings.get(1).name(),result.get("openingName1"));
        assertEquals(openings.get(2).name(),result.get("openingName2"));
        assertEquals(openings.get(0).thickness(),result.get("openingLead0"));
        assertEquals(openings.get(1).thickness(),result.get("openingLead1"));
        assertEquals(openings.get(2).thickness(),result.get("openingLead2"));

    }


}
