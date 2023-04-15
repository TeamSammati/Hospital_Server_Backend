package site.sammati_hospital.service;

import org.springframework.stereotype.Component;
import site.sammati_hospital.dto.*;
import site.sammati_hospital.entity.Doctor;
import site.sammati_hospital.entity.Episode;
import site.sammati_hospital.entity.Prescription;
import site.sammati_hospital.entity.Record;

import java.util.ArrayList;
import java.util.List;

@Component
public interface DoctorLoginService {
    Doctor loginDoctor(Credentials credentials);

    public Integer createEpisode(Integer patientId, String episodeType);

    public Integer createVisit(Integer patientId,Integer episodeId,Integer doctorId);

    public Integer addRecord(RecordDto recordDto);

    public void addPrescription(PrescriptionDto prescriptionDto);

    List<Record> findRecordsByPatientId(Integer patientId, Integer reqType);

    public Doctor findDoctorByEmail(String email);

    List<Episode> getEpisodes(Integer patientId);

    List<Record> findRecords(ArrayList<Integer> recordId);

    List<Record> findAllRecords(Integer pid);

    DoctorDetailsDTO getDoctorDetails(Integer doctorId);
}
