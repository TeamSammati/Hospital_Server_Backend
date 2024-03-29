package site.sammati_hospital.controller;
import io.jsonwebtoken.Header;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import site.sammati_hospital.authentication.AuthenticationService;
import site.sammati_hospital.entity.*;
import site.sammati_hospital.repository.EmergencyCRRepository;
import site.sammati_hospital.repository.PatientRepository;
import site.sammati_hospital.service.DoctorLoginService;
import site.sammati_hospital.dto.*;
import site.sammati_hospital.utils.enums.ConsentRequestStatus;
import site.sammati_hospital.utils.enums.ReqType;
import site.sammati_hospital.entity.Record;

import java.util.ArrayList;
import java.util.List;

import static site.sammati_hospital.service.PatientAuthService.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class HospitalController {


    private final Environment env;
    private final AuthenticationService service;
    @Autowired
    private DoctorLoginService doctorLoginService;

    private final EmergencyCRRepository emergencyCRRepository;
    @Autowired
    private PatientRepository patientRepository;

    @PostMapping("/add-consent-request")
    @PreAuthorize("hasAuthority('DOCTOR')")
    public Integer generateConsentRequest(@RequestBody ConsentRequest consentRequest){

        String uri = "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/add-consent-request";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+env.getProperty("app.sammati_token"));
        HttpEntity<ConsentRequest> request = new HttpEntity<ConsentRequest>(consentRequest, headers);

        ResponseEntity<Integer> response = restTemplate.postForEntity( uri, request , Integer.class);
        return response.getBody();
    }

//    @GetMapping("/get-status")
//    public List<Object> getConsentRequestStatus(@RequestParam("pId") Integer patientId, @RequestParam("dId") Integer doctorId, @RequestParam("hId") Integer hospitalId){
//        String uri = "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/get-status/"+patientId+"/"+doctorId+"/"+hospitalId;
//        //IP of Sammati server/API call
//        RestTemplate restTemplate = new RestTemplate();
//        List<Object> result = restTemplate.getForObject(uri, List.class);
//        return result;
//    }

    @GetMapping("/get-status-all")
    @PreAuthorize("hasAuthority('DOCTOR')")
    public ResponseEntity<Object> getConsentRequestStatusAll(@RequestParam("dId") Integer doctorId, @RequestParam("hId") Integer hospitalId){
        String uri = "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/get-status-all/"+doctorId+"/"+hospitalId;
        //IP of Sammati server/API call
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+env.getProperty("app.sammati_token"));
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, Object.class);
        return result;
    }

    @GetMapping("/get-pending-emergency-cr")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<EmergencyConsentRequest> getPendingEmergencyConsentRequest(){
        return emergencyCRRepository.findAllPendingRequests();
    }

//    @PostMapping("/login")
//    public Doctor DoctorLogin(@RequestBody Credentials credentials)
//    {
//        Doctor doctor=doctorLoginService.loginDoctor(credentials);
//        return doctor;
//    }

    @PostMapping("/add-episode")
    @PreAuthorize("hasAuthority('RECEPTIONIST')")
    public Integer addEpisode(@RequestParam("patientId") Integer patientId,@RequestParam("episodetype") String episodeType){
        return doctorLoginService.createEpisode(patientId,episodeType);
    }

    @PostMapping("/add-visit")
    @PreAuthorize("hasAuthority('RECEPTIONIST')")
    public Integer addVisit(@RequestParam("patientId") Integer patientId, @RequestParam("episodeId") Integer episodeId,@RequestParam("doctorId") Integer doctorId){
        return doctorLoginService.createVisit(patientId,episodeId,doctorId);
    }


    @PostMapping("/add-record")
    @PreAuthorize("hasAuthority('DOCTOR')")
    public Integer addRecord(@RequestBody RecPreDto recordDto){

//        System.out.println(recordDto);

        Integer recordId= doctorLoginService.addRecord(recordDto.getRecordDto());
        if(recordId==-99)return -99;
        for (PrescriptionDto prescriptionDto:recordDto.getPrescriptionDtos()) {
            prescriptionDto.setRecordId(recordId);
            doctorLoginService.addPrescription(prescriptionDto);
        }
//        doctorLoginService.addRecord(record);
        return recordId;
    }


//    @GetMapping("/send_records/{pid}/{reqType}")
//    public List<RecPreDto2> sendRecords(@PathVariable("pid") Integer patientId, @PathVariable("reqType")Integer reqType){
//        return doctorLoginService.findRecordsByPatientId(patientId,reqType);
//    }
//    @GetMapping("/send_records/{pid}/{reqType}")
//    public List<Prescription>  sendRecords(@PathVariable("pid") Integer patientId, @PathVariable("reqType")Integer reqType){
//        return doctorLoginService.findRecordsByPatientId(patientId,reqType);
//    }

    @GetMapping("/patient-exist-in-hospital")
    @PreAuthorize("hasAuthority('SAMMATI')")
    public ResponseEntity<Object> checkPatientExistInHospital(@RequestParam("patientId") Integer patientId ) {
        String uri = "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/patient-exist-in-hospital/" + patientId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+env.getProperty("app.sammati_token"));
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, Object.class);
        return result;
    }

    @GetMapping("/get-episodes")
    @PreAuthorize("hasAuthority('RECEPTIONIST')")
    public List<Episode> getEpisodes(@RequestParam("patientId") Integer patientId){
        List<Episode> episodes=doctorLoginService.getEpisodes(patientId);
        for(Episode episode:episodes){
            List<Visit> visits=episode.getVisits();
            for(Visit visit:visits){
                visit.setRecords(null);
            }
        }
    return episodes;
    }
    @PostMapping("/get-patient-data")
    @PreAuthorize("hasAuthority('RECEPTIONIST')")
    public PatientDto getPatientData(@RequestBody PatientOtpDto patientOtpDto)
    {
        String uri = "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/get-patient-data-by-patient-id";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+env.getProperty("app.sammati_token"));
        HttpEntity<PatientOtpDto> request = new HttpEntity<PatientOtpDto>(patientOtpDto, headers);
        PatientDto patientDto= restTemplate.postForObject(uri,request,PatientDto.class);
        System.out.println(patientDto);
        return patientDto;
    }

    @GetMapping("/send-otp-patient")
    @PreAuthorize("hasAuthority('RECEPTIONIST')")
    public ResponseEntity<Object> sendOtp(@RequestParam Integer patientId)
    {
        String uri = "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/generate-otp/" +patientId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+env.getProperty("app.sammati_token"));
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
//        boolean result=restTemplate.postForObject(uri,null,boolean.class);
        ResponseEntity<Object> result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, Object.class);
        return result;
    }

    @PostMapping("/register-new-patient")
    @PreAuthorize("hasAuthority('RECEPTIONIST')")
    public Integer registerPatient(@RequestBody PatientDto patientDto,@RequestParam Integer hospitalId)
    {
        System.out.println("In hospital");
        Integer pid=service.registerPatient(patientDto);
        String uri="http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/change-patient-hospital-mapping/"+patientDto.getPatientId()+"/"+hospitalId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+env.getProperty("app.sammati_token"));
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, Object.class);
//
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.postForObject(uri,null,void.class);
        return pid;
    }


    @GetMapping("/active-consents-doctor")
    @PreAuthorize("hasAuthority('DOCTOR')")
    public ResponseEntity<Object> activeConsent(@RequestParam Integer doctorId){
        String uri = "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/active-consents-doctor?doctorId="+doctorId+"&hospitalId="+env.getProperty("app.hospital_id");;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+env.getProperty("app.sammati_token"));
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, Object.class);
//
        return result;
    }

    @GetMapping("/send-records/{pid}/{reqType}")
    @PreAuthorize("hasAuthority('SAMMATI')")
    public List<Episode> sendRecords(@PathVariable("pid") Integer patientId, @PathVariable("reqType")Integer reqType){
        return doctorLoginService.getEpisodes(patientId);
    }



    @GetMapping("/get-all-hospital-with-doctors")
    @PreAuthorize("hasAuthority('DOCTOR')")
    public ResponseEntity<Object> getHospitalDoctorDetails()
    {

        String uri= "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/hospital-with-doctors";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+env.getProperty("app.sammati_token"));
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, Object.class);
        return result;
    }


    @PostMapping("/delegate")
    @PreAuthorize("hasAuthority('DOCTOR')")
    public Integer delegation(@RequestBody DelegationDto delegationDto)
    {
        String uri= "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/add-delegation-mapping";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+env.getProperty("app.sammati_token"));
        HttpEntity<DelegationDto> request = new HttpEntity<DelegationDto>(delegationDto, headers);
        return restTemplate.postForEntity( uri, request , Integer.class).getBody();
    }

    @GetMapping("/get-patient-records")
    @PreAuthorize("hasAuthority('DOCTOR')")
    public ResponseEntity<Object> getPatientRecords(@RequestParam("consentId") Integer consentId,@RequestParam("doctorId")Integer doctorId,@RequestParam("hospitalId") Integer hospitalId)
    {
        System.out.println("in hospital");
        String uri = "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/fetch-records-by-consent-id/" + consentId+"/"+doctorId+"/"+hospitalId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+env.getProperty("app.sammati_token"));
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, Object.class);
        return result;
    }

    @PostMapping("/send-patient-records")
    @PreAuthorize("hasAuthority('SAMMATI')")
    public List<Record> sendPatientData(@RequestBody ArrayList<Integer> records)
    {
        System.out.println(records);
        return  doctorLoginService.findRecords(records);
    }

    @GetMapping("/doctor-details")
    @PreAuthorize("hasAnyAuthority('ADMIN','DOCTOR')")
    public DoctorDetailsDTO getDoctorDetails(@RequestParam("doctorId") Integer doctorId){
        return doctorLoginService.getDoctorDetails(doctorId);
    }

    @GetMapping("/send-all-patient-records")
    @PreAuthorize("hasAuthority('SAMMATI')")
    public List<Record> sendAllPatientData(@RequestParam Integer patientId)
    {
        return  doctorLoginService.findAllRecords(patientId);
    }

    @PostMapping("/add-emergency-consent-request")
    @PreAuthorize("hasAuthority('DOCTOR')")
    public ResponseEntity<Object> addEmergencyCR(@RequestBody EmergencyConsentRequest emergencyConsentRequest){
        return new ResponseEntity<>(doctorLoginService.addEmergencyCR(emergencyConsentRequest),HttpStatus.OK);
    }

    @PostMapping("/accept-emergency-consent")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> acceptEmergencyCR(@RequestParam Integer emergencyConsentRequestId,
                                                    @RequestParam ConsentRequestStatus consentRequestStatus,
                                                    @RequestParam Integer authId){

        if(consentRequestStatus==ConsentRequestStatus.REJECTED){
            return new ResponseEntity<>(doctorLoginService.acceptEmergencyCR(emergencyConsentRequestId, ConsentRequestStatus.REJECTED), HttpStatus.OK);
        }else {

            EmergencyConsentRequest emergencyConsentRequest = doctorLoginService.getEmergencyCRbyId(emergencyConsentRequestId);
            Patient patient=patientRepository.findByPatientId(emergencyConsentRequest.getPatientId());
            PatientDto patientDto;
            if(patient==null) {
                String uri = "http://" + env.getProperty("app.sammati_server") + ":" + env.getProperty("app.sammati_port") + "/get-emergency-patient-data-by-patient-id?patientId=" + emergencyConsentRequest.getPatientId() + "&hospitalId=" + emergencyConsentRequest.getHospitalId();
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", "Bearer " + env.getProperty("app.sammati_token"));
                HttpEntity<Void> request = new HttpEntity<>(headers);
                patientDto = restTemplate.postForEntity(uri, request, PatientDto.class).getBody();
                Integer pid = service.registerPatient(patientDto);
            }
            String uri2 = "http://" + env.getProperty("app.sammati_server") + ":" + env.getProperty("app.sammati_port") + "/grant-emergency-consent?pId="
                    + emergencyConsentRequest.getPatientId() + "&dId=" +
                    emergencyConsentRequest.getDoctorId() + "&hId=" +
                    emergencyConsentRequest.getHospitalId() + "&authId=" + authId +
                    "&phone=" + patientRepository.getPhoneNumber(emergencyConsentRequest.getPatientId());
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers2 = new HttpHeaders();
            headers2.setContentType(MediaType.APPLICATION_JSON);
            headers2.set("Authorization", "Bearer " + env.getProperty("app.sammati_token"));
            HttpEntity request2 = new HttpEntity<>(headers2);
            restTemplate.postForEntity(uri2, request2, Integer.class);

            return new ResponseEntity<>(doctorLoginService.acceptEmergencyCR(emergencyConsentRequestId, consentRequestStatus), HttpStatus.OK);
        }
        }
}
