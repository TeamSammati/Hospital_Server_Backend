package site.sammati_hospital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer prescriptionId;

    @ManyToOne
    @JoinColumn(name="recordId",nullable = false)
    private Record record;

    @Column(nullable = false)
    private String medicine;

    @Column(nullable = false)
    private Integer dosage;

    @Column(nullable = false)
    private String dosage_timing;

}
