package site.sammati_hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.sammati_hospital.entity.Record;
import site.sammati_hospital.entity.Visit;

public interface RecordRepository extends JpaRepository<Record, Integer> {
    Record findByRecordId(Integer recordId);
}
