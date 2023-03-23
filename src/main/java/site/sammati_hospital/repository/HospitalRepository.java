package site.sammati_hospital.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.sammati_hospital.entity.Doctor;

import javax.print.Doc;
import java.util.Optional;

@Repository
public interface HospitalRepository extends JpaRepository<Doctor, Integer>
{
    Optional<Doctor> findOneByDoctorIdAndPassword(Integer doctorId, String password);
    Doctor findByDoctorId(Integer doctorId);
}

