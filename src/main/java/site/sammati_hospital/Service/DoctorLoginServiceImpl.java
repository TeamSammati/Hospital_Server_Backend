package site.sammati_hospital.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.sammati_hospital.dto.Credentials;
import site.sammati_hospital.dto.LoginResponse;
import site.sammati_hospital.entity.Doctor;
import site.sammati_hospital.entity.Episode;
import site.sammati_hospital.entity.Visit;
import site.sammati_hospital.repository.HospitalRepository;
import site.sammati_hospital.repository.EpisodeRepository;
import site.sammati_hospital.repository.VisitRepository;

import java.sql.Date;
import java.time.LocalDate;

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
    public void createEpisode(Integer patientId, String episodeType){
        Date date = Date.valueOf(LocalDate.now());

        Episode episode=Episode.builder()
                .patientId(patientId)
                .start_date(date)
                .episode_type(episodeType)
                .build();
        episodeRepository.save(episode);
    }

    @Autowired
    private VisitRepository visitRepository;
    public void createVisit(Integer patientId, Integer episodeId,Integer doctorId){
        Date date = Date.valueOf(LocalDate.now());

        Episode episode=episodeRepository.findByEpisodeId(episodeId);
        Doctor doctor=hospitalRepository.findByDoctorId(doctorId);

        Visit visit=Visit.builder()
                .patientId(patientId)
                .episode(episode)
                .visit_date(date)
                .doctor(doctor)
                .build();
        visitRepository.save(visit);
    }
}
