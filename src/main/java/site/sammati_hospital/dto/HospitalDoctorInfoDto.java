package site.sammati_hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitalDoctorInfoDto
{
    private Integer hospitalId;
    private String hospitalName;
    private List<DoctorDto> doctorDto;
}
