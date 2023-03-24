package site.sammati_hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.sammati_hospital.entity.Doctor;
import site.sammati_hospital.entity.Record;
import site.sammati_hospital.entity.Visit;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecordDto {
    Integer patientId;
    Integer doctorId;
    Integer visitId;
    String problem;
    String treatment;

}
