package site.sammati_hospital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recordId;
    @Column(nullable = false)
    private Integer patientId;

    @ManyToOne
    @JoinColumn(name="doctorId",nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name="visitId",nullable = false)
    private Visit visit;

    @Column(nullable = false)
    private String problem;

    @Column(nullable = false)
    private String treatment;

    @OneToMany(mappedBy = "record")
    private List<Prescription> prescriptions;
}
