package site.sammati_hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto
{
    private Integer patientId;
    private String firstName;
    private String LastName;
    private String phoneNumber;
    private String gender;
    private Date DOB;
    private String state;
    private String address;
    private String pinCode;
    private String passPhoto;

}
