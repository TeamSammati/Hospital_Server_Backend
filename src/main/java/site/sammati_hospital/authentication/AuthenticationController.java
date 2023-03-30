package site.sammati_hospital.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import site.sammati_hospital.dto.DoctorHospitalDto;
import site.sammati_hospital.dto.PatientDoctorMapping;
import site.sammati_hospital.dto.PatientDto;
import site.sammati_hospital.entity.Doctor;
import site.sammati_hospital.entity.Episode;
import site.sammati_hospital.entity.Prescription;
import site.sammati_hospital.entity.Record;
import site.sammati_hospital.service.DoctorLoginService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController
{

    private final AuthenticationService service;
    private final DoctorLoginService doctorLoginService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody Doctor doctor) {

        AuthenticationResponse authenticationResponse= service.register(doctor);
        Doctor doctor2=doctorLoginService.findDoctorByEmail(doctor.getEmail());
        authenticationResponse.setDoctor(doctor2);
        String uri= "http://172.16.131.147:6979/add-doctor-hospital-mapping";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        DoctorHospitalDto mapping = new DoctorHospitalDto();
        System.out.println();
        mapping.setDoctorId(authenticationResponse.getDoctor().getDoctorId());
        mapping.setDoctorName(authenticationResponse.getDoctor().getFirstName()+" "+authenticationResponse.getDoctor().getLastName());
        mapping.setHospitalId(1);
        mapping.setHospitalName("Appolo");
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

    @PostMapping("/register_new_patient")
    public Integer registerPatient(@RequestBody PatientDto patientDto)
    {

        String uri1 = "http://172.16.133.184:6979/global_patient_id_exist/" + patientDto.getPatientId();
        RestTemplate restTemplate = new RestTemplate();
        Boolean response1= restTemplate.postForObject(uri1, null, Boolean.class);
        if(response1==false)return Integer.MIN_VALUE;

        String uri2 = "http://172.16.133.184:6979/add_patient_hospital_mapping";
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

    @PostMapping("/send_patient_records")
    public List<Record> sendPatientData(@RequestBody ArrayList<Integer> records)
    {
        System.out.println(records);
        return  doctorLoginService.findRecords(records);
    }

}
