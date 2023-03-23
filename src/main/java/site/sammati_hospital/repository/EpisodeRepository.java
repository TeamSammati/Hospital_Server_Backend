package site.sammati_hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.sammati_hospital.entity.Episode;


@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Integer> {

}
