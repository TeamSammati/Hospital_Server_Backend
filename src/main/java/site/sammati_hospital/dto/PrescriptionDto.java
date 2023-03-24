package site.sammati_hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.sammati_hospital.entity.Record;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionDto {
    Integer recordId;
    String medicine;
    Integer dosage;
    String dosage_timing;
}
