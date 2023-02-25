package site.sammati_hospital.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.sammati_hospital.dto.Credentials;
import site.sammati_hospital.dto.LoginResponse;
import site.sammati_hospital.entity.Doctor;
import site.sammati_hospital.repository.HospitalRepository;
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
}
