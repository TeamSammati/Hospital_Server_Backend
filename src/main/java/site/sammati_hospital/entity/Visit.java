package site.sammati_hospital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Visit {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer visitId;
    @Column(nullable = false)
    private Integer patientId;

    @ManyToOne
    @JoinColumn(name="episodeId",nullable = false)
    private Episode episode;

    @Column(nullable = false)
    private Date doctorId;

    @Column(nullable = false)
    private Date visit_date;

    @OneToMany(mappedBy = "visit")
    private List<Record> records;

}
