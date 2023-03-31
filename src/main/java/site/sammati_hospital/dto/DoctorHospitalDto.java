package site.sammati_hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorHospitalDto
{
    private Integer doctorId;
    private Integer hospitalId;
    private String doctorName;
    private String hospitalName;
}
