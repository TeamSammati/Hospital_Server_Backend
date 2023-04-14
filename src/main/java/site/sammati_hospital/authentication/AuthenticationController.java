package site.sammati_hospital.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import site.sammati_hospital.dto.DoctorHospitalDto;
import site.sammati_hospital.dto.*;
import site.sammati_hospital.entity.*;
import site.sammati_hospital.entity.Record;
import site.sammati_hospital.service.DoctorLoginService;

import java.util.ArrayList;
import java.util.List;

import static site.sammati_hospital.service.PatientAuthService.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController
{
    private final Environment env;
    private final AuthenticationService service;
    private final DoctorLoginService doctorLoginService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody Doctor doctor) {

        AuthenticationResponse authenticationResponse= service.register(doctor);
        Doctor doctor2=doctorLoginService.findDoctorByEmail(doctor.getEmail());
        authenticationResponse.setDoctor(doctor2);
        String uri= "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/add-doctor-hospital-mapping";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+env.getProperty("app.sammati_token"));
        DoctorHospitalDto mapping = new DoctorHospitalDto();
        System.out.println();
        mapping.setDoctorId(authenticationResponse.getDoctor().getDoctorId());
        mapping.setDoctorName(authenticationResponse.getDoctor().getFirstName()+" "+authenticationResponse.getDoctor().getLastName());
        mapping.setHospitalId(Integer.valueOf(env.getProperty("app.hospital_id")));
        mapping.setHospitalName(env.getProperty("app.hospital_name"));
        HttpEntity<DoctorHospitalDto> request1 = new HttpEntity<DoctorHospitalDto>(mapping, headers);
        ResponseEntity<Integer> response = restTemplate.postForEntity( uri, request1 , Integer.class);
        return ResponseEntity.ok(authenticationResponse);
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request
    ) {
        Doctor doctor=doctorLoginService.findDoctorByEmail(request.getEmail());
        AuthenticationResponse authenticationResponse=AuthenticationResponse.builder()
                .doctor(doctor)
                .token(service.authenticate(request).getToken())
                .build();
        return ResponseEntity.ok(authenticationResponse);
    }

}
