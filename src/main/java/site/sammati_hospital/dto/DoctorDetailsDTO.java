package site.sammati_hospital.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.sammati_hospital.entity.Role;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorDetailsDTO {
    private Integer doctorId;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private Integer registrationNumber;
    private Date registrationDate;
    private String designation;
    private String email;
    private Role role;
}
