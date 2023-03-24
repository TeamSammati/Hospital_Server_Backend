package site.sammati_hospital.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.sammati_hospital.dto.*;
import site.sammati_hospital.entity.*;
import site.sammati_hospital.entity.Record;
import site.sammati_hospital.repository.*;
import site.sammati_hospital.utils.enums.ReqType;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorLoginServiceImpl implements DoctorLoginService{
    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Doctor loginDoctor(Credentials credentials)
    {
        Doctor doctor=hospitalRepository.findByDoctorId(credentials.getDoctorId());
        if(doctor!=null)
        {
            String password= credentials.getPassword();
            String encodedPassword=doctor.getPassword();
            Boolean encryptedPassword=passwordEncoder.matches(password,encodedPassword);
            if(encryptedPassword)
            {
                return doctor;
            }
            else
            {
                return null;
            }
        }
        else {
            return null;
        }
    }

    @Autowired
    private EpisodeRepository episodeRepository;
    public Integer createEpisode(Integer patientId, String episodeType){
        Date date = Date.valueOf(LocalDate.now());

        Episode episode=Episode.builder()
                .patientId(patientId)
                .start_date(date)
                .episode_type(episodeType)
                .build();
        return episodeRepository.save(episode).getEpisodeId();

    }

    @Autowired
    private VisitRepository visitRepository;
    public Integer createVisit(Integer patientId, Integer episodeId,Integer doctorId){
        Date date = Date.valueOf(LocalDate.now());

        Episode episode=episodeRepository.findByEpisodeId(episodeId);
        Doctor doctor=hospitalRepository.findByDoctorId(doctorId);

        Visit visit=Visit.builder()
                .patientId(patientId)
                .episode(episode)
                .visit_date(date)
                .doctor(doctor)
                .build();
        return visitRepository.save(visit).getVisitId();
    }

    @Autowired
    private RecordRepository recordRepository;
    public Integer addRecord(RecordDto recordDto){

        Visit visit=visitRepository.findByVisitId(recordDto.getVisitId());
        Doctor doctor=hospitalRepository.findByDoctorId(recordDto.getDoctorId());

        Record record1= Record.builder()
                .patientId(recordDto.getPatientId())
                .problem(recordDto.getProblem())
                .treatment(recordDto.getTreatment())
                .doctor(doctor)
                .visit(visit)
                .build();
        return recordRepository.save(record1).getRecordId();

//        addPrescription(record);
    }

    @Autowired
    private PrescriptionRepository prescriptionRepository;
    public void addPrescription(PrescriptionDto prescriptionDto){

        Record record=recordRepository.findByRecordId(prescriptionDto.getRecordId());
        Prescription prescription= Prescription.builder()
                .record(record)
                .medicine(prescriptionDto.getMedicine())
                .dosage(prescriptionDto.getDosage())
                .dosage_timing(prescriptionDto.getDosage_timing())
                .build();
        prescriptionRepository.save(prescription);


    }


    @Override
    public List<RecPreDto2> findRecordsByPatientId(Integer patientId, Integer reqType) {
        List<RecPreDto2> records=new ArrayList<>();
        if(reqType==0){
            List<Record> recordList=recordRepository.findAllByPatientId(patientId);
            for(Record record:recordList){
                List<Prescription> prescriptions=prescriptionRepository.findAllByRecordId(record.getRecordId());
                records.add(new RecPreDto2(record,prescriptions));
            }
        }
        return records;
    }


}
