package site.sammati_hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.sammati_hospital.entity.Prescription;
import site.sammati_hospital.entity.Record;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecPreDto2 {
    private Record record;
    private List<Prescription> prescription;
}

