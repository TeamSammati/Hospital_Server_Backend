package site.sammati_hospital.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import site.sammati_hospital.dto.PatientDoctorMapping;
import site.sammati_hospital.dto.PatientDto;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController
{

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
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


}
