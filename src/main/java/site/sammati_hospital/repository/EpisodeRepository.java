package site.sammati_hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.sammati_hospital.entity.Episode;

import java.util.List;
import java.util.Optional;


@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Integer> {

    Episode findByEpisodeId(Integer episodeId);

    List<Episode> findAllByPatientId(Integer patientId);
}
