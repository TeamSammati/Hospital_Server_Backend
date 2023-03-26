package site.sammati_hospital.service;

import org.springframework.stereotype.Component;
import site.sammati_hospital.dto.*;
import site.sammati_hospital.entity.Doctor;
import site.sammati_hospital.entity.Prescription;

import java.util.List;

@Component
public interface DoctorLoginService {
    Doctor loginDoctor(Credentials credentials);

    public Integer createEpisode(Integer patientId, String episodeType);

    public Integer createVisit(Integer patientId,Integer episodeId,Integer doctorId);

    public Integer addRecord(RecordDto recordDto);

    public void addPrescription(PrescriptionDto prescriptionDto);

    List<Prescription> findRecordsByPatientId(Integer patientId, Integer reqType);
}
