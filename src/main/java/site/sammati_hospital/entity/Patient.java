package site.sammati_hospital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Patient
{
    @Id
    private Integer patientId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String gender;

    @Column(unique = true)
    private String UID_Number;

    private String UID_type;

    @Column(nullable = false, unique = true)
    private String email;

    private Date DOB;

    private String state;

    private String address;

    private String pinCode;

    private String passPhoto;

    @Column(nullable = false)
    private float weight;

    @Column(nullable = false)
    private Integer age;

    private String bloodGroup;


}