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
    private String lastName;
    private String phoneNumber;
    private String gender;
    private String UID_Number;
    private String UID_type;
    private String email;
    private Date DOB;
    private String state;
    private String address;
    private String pinCode;
    private String passPhoto;
    private float weight;
    private Integer age;
    private String bloodGroup;
}
