package site.sammati_hospital.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.sammati_hospital.utils.enums.ConsentRequestStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsentRequest {
    private Integer consentRequestId;
    private Integer patientId;
    private Integer doctorId;
    private Integer hospitalId;
    private ConsentRequestStatus consentRequestStatus;
}