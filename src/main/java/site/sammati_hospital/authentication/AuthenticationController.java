package site.sammati_hospital.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import site.sammati_hospital.dto.PatientDoctorMapping;
import site.sammati_hospital.dto.PatientDto;
import site.sammati_hospital.dto.RecPreDto2;
import site.sammati_hospital.entity.Doctor;
import site.sammati_hospital.entity.Episode;
import site.sammati_hospital.entity.Prescription;
import site.sammati_hospital.entity.Record;
import site.sammati_hospital.service.DoctorLoginService;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController
{

    private final AuthenticationService service;
    private final DoctorLoginService doctorLoginService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
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

    @PostMapping("/register_new_patient")
    public Integer registerPatient(@RequestBody PatientDto patientDto)
    {

        String uri1 = "http://172.16.131.147:6979/global_patient_id_exist/" + patientDto.getPatientId();
        RestTemplate restTemplate = new RestTemplate();
        Boolean response1= restTemplate.postForObject(uri1, null, Boolean.class);
        if(response1==false)return Integer.MIN_VALUE;

        String uri2 = "http://172.16.131.147:6979/add_patient_hospital_mapping";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        PatientDoctorMapping mapping = new PatientDoctorMapping();
        mapping.setPatientId(patientDto.getPatientId());
        mapping.setHospitalId(10);
        HttpEntity<PatientDoctorMapping> request = new HttpEntity<PatientDoctorMapping>(mapping, headers);
        ResponseEntity<Integer> response2 = restTemplate.postForEntity( uri2, request , Integer.class);
        if(response2.getBody()!=Integer.MAX_VALUE)
        {
            System.out.println("Mapping save successfully");
        }
        return service.registerPatient(patientDto);
    }

    @GetMapping("/send_records/{pid}/{reqType}")
    public List<Episode> sendRecords(@PathVariable("pid") Integer patientId, @PathVariable("reqType")Integer reqType){
        return doctorLoginService.getEpisodes(patientId);
    }

}
