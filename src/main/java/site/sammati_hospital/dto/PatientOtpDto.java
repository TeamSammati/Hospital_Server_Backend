package site.sammati_hospital.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientOtpDto
{
    private Integer hospitalId;
    private Integer patientId;
    private String otp;
}
