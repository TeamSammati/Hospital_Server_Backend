package site.sammati_hospital.controller;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import site.sammati_hospital.authentication.AuthenticationService;
import site.sammati_hospital.entity.*;
import site.sammati_hospital.service.DoctorLoginService;
import site.sammati_hospital.dto.*;
import site.sammati_hospital.utils.enums.ReqType;
import site.sammati_hospital.entity.Record;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class HospitalController {


    private final Environment env;
    private final AuthenticationService service;
    @Autowired
    private DoctorLoginService doctorLoginService;

    @PostMapping("/add-consent-request")
    public Integer generateConsentRequest(@RequestBody ConsentRequest consentRequest){
        String uri = "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/add-consent-request";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ConsentRequest> request = new HttpEntity<ConsentRequest>(consentRequest, headers);

        ResponseEntity<Integer> response = restTemplate.postForEntity( uri, request , Integer.class);
        return response.getBody();
    }

    @GetMapping("/get-status")
    public List<Object> getConsentRequestStatus(@RequestParam("pId") Integer patientId, @RequestParam("dId") Integer doctorId, @RequestParam("hId") Integer hospitalId){
        String uri = "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/get-status/"+patientId+"/"+doctorId+"/"+hospitalId;
        //IP of Sammati server/API call
        RestTemplate restTemplate = new RestTemplate();
        List<Object> result = restTemplate.getForObject(uri, List.class);
        return result;
    }

    @GetMapping("/get-status-all")
    public List<Object> getConsentRequestStatusAll(@RequestParam("dId") Integer doctorId, @RequestParam("hId") Integer hospitalId){
        String uri = "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/get-status-all/"+doctorId+"/"+hospitalId;
        //IP of Sammati server/API call
        RestTemplate restTemplate = new RestTemplate();
        List<Object> result = restTemplate.getForObject(uri, List.class);
        return result;
    }

    @PostMapping("/login")
    public Doctor DoctorLogin(@RequestBody Credentials credentials)
    {
        Doctor doctor=doctorLoginService.loginDoctor(credentials);
        return doctor;
    }

    @PostMapping("/add-episode")
    public Integer addEpisode(@RequestParam("patientId") Integer patientId,@RequestParam("episodetype") String episodeType){
        return doctorLoginService.createEpisode(patientId,episodeType);
    }

    @PostMapping("/add-visit")
    public Integer addVisit(@RequestParam("patientId") Integer patientId, @RequestParam("episodeId") Integer episodeId,@RequestParam("doctorId") Integer doctorId){
        return doctorLoginService.createVisit(patientId,episodeId,doctorId);
    }


    @PostMapping("/add-record")
    public Integer addRecord(@RequestBody RecPreDto recordDto){

//        System.out.println(record);

        Integer recordId= doctorLoginService.addRecord(recordDto.getRecordDto());
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
    public Boolean checkPatientExistInHospital(@RequestParam("patientId") Integer patientId ) {
        String uri = "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/patient-exist-in-hospital/" + patientId;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Boolean> response = restTemplate.getForEntity(uri, Boolean.class);
        System.out.println(response.getBody());
        return response.getBody();
    }

    @GetMapping("/get-episodes")
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

    @PostMapping("/get-patient-records")
    public List<Record> getPatientRecords(@RequestParam Integer consentRequestId)
    {
        String uri = "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/fetch-records-by-consent-request-id/" + consentRequestId;
        RestTemplate restTemplate = new RestTemplate();
        List<Record> data = restTemplate.postForObject(uri,null, List.class);
        return data;
    }

    @PostMapping("/get-patient-data")
    public PatientDto getPatientData(@RequestBody PatientOtpDto patientOtpDto)
    {
        String uri = "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/get-patient-data-by-patient-id";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PatientOtpDto> request = new HttpEntity<PatientOtpDto>(patientOtpDto, headers);
        PatientDto patientDto= restTemplate.postForObject(uri,request,PatientDto.class);
        System.out.println(patientDto);
        return patientDto;
    }

    @PostMapping("/send-otp-patient")
    public Boolean sendOtp(@RequestParam Integer patientId)
    {
        String uri = "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/generate-otp/" +patientId;
        RestTemplate restTemplate = new RestTemplate();
        boolean result=restTemplate.postForObject(uri,null,boolean.class);
        return result;
    }

    @PostMapping("/register-new-patient")
    public Integer registerPatient(@RequestBody PatientDto patientDto,@RequestParam Integer hospitalId)
    {
        System.out.println("In hospital");
        Integer pid=service.registerPatient(patientDto);
        String uri="http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/change-patient-hospital-mapping/"+patientDto.getPatientId()+"/"+hospitalId;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(uri,null,void.class);
        return pid;
    }





}
