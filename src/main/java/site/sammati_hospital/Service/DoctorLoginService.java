package site.sammati_hospital.Service;

import org.springframework.stereotype.Component;
import site.sammati_hospital.dto.Credentials;
import site.sammati_hospital.dto.LoginResponse;
import site.sammati_hospital.entity.Doctor;

@Component
public interface DoctorLoginService {
    Doctor loginDoctor(Credentials credentials);

    public void createEpisode(Integer patientId, String episodeType);

    public void createVisit(Integer patientId,Integer episodeId,Integer doctorId);

}
