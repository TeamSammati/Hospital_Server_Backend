package site.sammati_hospital.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name="episodeId",nullable = false)
    private Episode episode;

    @ManyToOne
    @JoinColumn(name="doctorId",nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private Date visit_date;

    @OneToMany(mappedBy = "visit",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Record> records;

}
