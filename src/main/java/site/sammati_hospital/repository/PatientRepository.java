package site.sammati_hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.sammati_hospital.entity.Patient;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Integer>
{
    @Query("select p.phoneNumber from Patient p where p.patientId=?1")
    String getPhoneByPid(Integer patientId);

    Patient findByPatientId(Integer patientId);

    @Query("select p.phoneNumber from Patient p where p.patientId=?1")
    String getPhoneNumber(Integer patientId);
}