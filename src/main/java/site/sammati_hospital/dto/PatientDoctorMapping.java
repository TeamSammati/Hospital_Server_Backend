package site.sammati_hospital.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.Inet4Address;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDoctorMapping
{
    private Integer patientId;
    private Integer hospitalId;
}
