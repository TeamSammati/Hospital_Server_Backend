package site.sammati_hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.sammati_hospital.entity.Prescription;
import site.sammati_hospital.entity.Record;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
    @Query(value = "select * from prescription where record_id=?1",nativeQuery = true)
    List<Prescription> findAllByRecordId(Integer recordId);

}
