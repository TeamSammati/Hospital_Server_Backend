package site.sammati_hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.sammati_hospital.entity.EmergencyConsentRequest;

import java.util.List;

@Repository
public interface EmergencyCRRepository extends JpaRepository<EmergencyConsentRequest, Integer> {

    @Query(value = "select * from emergency_consent_request where consent_request_status=0",nativeQuery = true)
    List<EmergencyConsentRequest> findAllPendingRequests();
}
