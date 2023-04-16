package site.sammati_hospital.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.sammati_hospital.entity.EmergencyConsentRequest;
import site.sammati_hospital.utils.enums.ConsentRequestStatus;

import java.util.List;

@Repository
public interface EmergencyCRRepository extends JpaRepository<EmergencyConsentRequest, Integer> {

    @Query(value = "select * from emergency_consent_request where consent_request_status=0",nativeQuery = true)
    List<EmergencyConsentRequest> findAllPendingRequests();

    @Modifying
    @Transactional
    @Query("update EmergencyConsentRequest set consentRequestStatus=?2 where emergencyConsentRequestId=?1")
    Integer updateStatus(Integer emergencyConsentRequestId, ConsentRequestStatus consentRequestStatus);

    EmergencyConsentRequest findByEmergencyConsentRequestId(Integer id);
}
