package site.sammati_hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.sammati_hospital.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Integer>
{
    //
}