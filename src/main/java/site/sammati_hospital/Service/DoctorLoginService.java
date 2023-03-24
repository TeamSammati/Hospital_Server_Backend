package site.sammati_hospital.Service;

import org.springframework.stereotype.Component;
import site.sammati_hospital.dto.Credentials;
import site.sammati_hospital.dto.LoginResponse;
import site.sammati_hospital.dto.PrescriptionDto;
import site.sammati_hospital.dto.RecordDto;
import site.sammati_hospital.entity.Doctor;
import site.sammati_hospital.entity.Record;

@Component
public interface DoctorLoginService {
    Doctor loginDoctor(Credentials credentials);

    public Integer createEpisode(Integer patientId, String episodeType);

    public Integer createVisit(Integer patientId,Integer episodeId,Integer doctorId);

    public Integer addRecord(RecordDto recordDto);

    public void addPrescription(PrescriptionDto prescriptionDto);

}
