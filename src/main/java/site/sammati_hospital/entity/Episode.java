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
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer episodeId;
    @Column(nullable = false)
    private Integer patientId;

    @Column(nullable = false)
    private Date start_date;

    @Column
    private Date end_date;
    @Column(nullable = false)
    private String episode_type;

    @OneToMany(mappedBy = "episode",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Visit> visits;
}
