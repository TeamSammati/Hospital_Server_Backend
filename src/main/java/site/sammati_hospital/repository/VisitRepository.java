package site.sammati_hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.sammati_hospital.entity.Episode;
import site.sammati_hospital.entity.Visit;

import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Integer> {

    Visit findByVisitId(Integer visitId);

    List<Visit> findByPatientId(Integer patientId);
}
