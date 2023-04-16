package site.sammati_hospital.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.sammati_hospital.utils.enums.ConsentRequestStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EmergencyConsentRequest {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer emergencyConsentRequestId;
        @Column(nullable = false)
        private Integer patientId;
        @Column(nullable = false)
        private Integer doctorId;
        @Column(nullable = false)
        private String doctorName;
        @Column(nullable = false)
        private Integer hospitalId;
        private ConsentRequestStatus consentRequestStatus;
        private String purpose;
}