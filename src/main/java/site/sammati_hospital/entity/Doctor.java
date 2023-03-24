package site.sammati_hospital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Doctor
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer doctorId;
    @Column(nullable = false)
    private String firstName;
    private String lastName;
    @Column(nullable = false, unique = true)
    private String mobileNumber;
    @Column(nullable = false, unique = true)
    private Integer registrationNumber;
    private Date registrationDate;
    private String designation;
    private String password;
    @Column(nullable = false, unique = true)
    private String email;


    @Column(nullable = false)
    private Integer patientId;

    @OneToMany(mappedBy = "doctor")
    private List<Visit> visits;
    @OneToMany(mappedBy = "doctor")
    private List<Record> record;
}

