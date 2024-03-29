package site.sammati_hospital.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import site.sammati_hospital.config.JwtService;
import site.sammati_hospital.dto.DoctorHospitalDto;
import site.sammati_hospital.dto.PatientDoctorMapping;
import site.sammati_hospital.dto.PatientDto;
import site.sammati_hospital.entity.*;
import site.sammati_hospital.repository.HospitalRepository;
import site.sammati_hospital.repository.PatientRepository;
import site.sammati_hospital.repository.TokenRepository;

import java.sql.Date;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final HospitalRepository repository;
    private final TokenRepository tokenRepository;

    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(Doctor request) {

        var user = Doctor.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .mobileNumber(request.getMobileNumber())
                .registrationNumber(request.getRegistrationNumber())
                .registrationDate(Date.valueOf(LocalDate.now()))
                .designation(request.getDesignation())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user,request.getRole());
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user,user.getRole());
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void saveUserToken(Doctor doctor, String jwtToken) {
        var token = Token.builder()
                .doctor(doctor)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Doctor doctor) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(doctor.getDoctorId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public Integer registerPatient(PatientDto patientDto)
    {
        var patient = Patient.builder()
                .patientId(patientDto.getPatientId())
                .firstName(patientDto.getFirstName())
                .lastName(patientDto.getLastName())
                .phoneNumber(patientDto.getPhoneNumber())
                .gender(patientDto.getGender())
                .DOB(patientDto.getDOB())
                .state(patientDto.getState())
                .address(patientDto.getAddress())
                .pinCode(patientDto.getPinCode())
                .passPhoto(patientDto.getPassPhoto())
                .build();
        patientRepository.save(patient);
        return patient.getPatientId();
    }
}
