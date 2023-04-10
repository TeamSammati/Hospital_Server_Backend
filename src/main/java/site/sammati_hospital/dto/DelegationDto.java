package site.sammati_hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DelegationDto
{
    private Integer consentId;
    private Integer doctorId;
    private String  doctorName;
    private Integer hospitalId;
    private Integer duration;
}
