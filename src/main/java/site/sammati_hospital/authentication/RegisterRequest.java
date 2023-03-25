package site.sammati_hospital.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.sammati_hospital.entity.Role;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String mobileNumber;
    private Integer registrationNumber;
    private Date registrationDate;
    private String designation;
    private String password;
    private String email;
    private Role role;
}
