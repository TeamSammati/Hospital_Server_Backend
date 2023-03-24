package site.sammati_hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.sammati_hospital.entity.Prescription;
import site.sammati_hospital.entity.Record;

public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
}
