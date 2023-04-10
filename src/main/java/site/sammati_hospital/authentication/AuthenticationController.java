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

    @GetMapping("/send-records/{pid}/{reqType}")
    public List<Episode> sendRecords(@PathVariable("pid") Integer patientId, @PathVariable("reqType")Integer reqType){
        return doctorLoginService.getEpisodes(patientId);
    }

    @PostMapping("/send-patient-records")
    public List<Record> sendPatientData(@RequestBody ArrayList<Integer> records)
    {
        System.out.println(records);
        return  doctorLoginService.findRecords(records);
    }

    //whitelist for checking
    @PostMapping("/get-all-hospital-with-doctors")
    public List<HospitalDoctorInfoDto> getHospitalDoctorDetails()
    {

        String uri= "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/hospital-with-doctors";
        RestTemplate restTemplate = new RestTemplate();
        List<HospitalDoctorInfoDto> hospitalDoctorInfoDto=restTemplate.postForObject(uri,null, List.class);
        return hospitalDoctorInfoDto;
    }

    //whitelist for checking
    @PostMapping("/delegate")
    public void delegation(@RequestBody DelegationDto delegationDto)
    {
        String uri= "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/add-delegation-mapping";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DelegationDto> request = new HttpEntity<DelegationDto>(delegationDto, headers);
        restTemplate.postForEntity( uri, request , void.class);
    }

    //whitelist for checking
    @PostMapping("/get-patient-records")
    public List<Record> getPatientRecords(@RequestParam("consentId") Integer consentId,@RequestParam("doctorId")Integer doctorId,@RequestParam("hospitalId") Integer hospitalId)
    {
        System.out.println("in hospital");
        String uri = "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/fetch-records-by-consent-id/" + consentId+"/"+doctorId+"/"+hospitalId;
        RestTemplate restTemplate = new RestTemplate();
        List<Record> data = restTemplate.postForObject(uri,null, List.class);
        return data;
    }

    @GetMapping("/authorize-patient")
    public String authorizePatient(@RequestParam("patientId") Integer pid) {
        clearOTPFromCache(pid.toString());
        String otp = genString(pid.toString());
        System.out.println(otp);
        return otp;
    }

    @GetMapping("/validate-patient")
    public boolean validatePatient(@RequestParam("patientId") Integer pid, @RequestParam("str") String str) {
        String pto = getStrByPID(pid.toString());
        if(pto==null)
            return false;
//        System.out.println("Jai Sree Ram");
        return str.equals(pto);
    }

}
