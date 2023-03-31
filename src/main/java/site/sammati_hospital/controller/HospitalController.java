package site.sammati_hospital.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import site.sammati_hospital.entity.*;
import site.sammati_hospital.service.DoctorLoginService;
import site.sammati_hospital.dto.*;
import site.sammati_hospital.utils.enums.ReqType;
import site.sammati_hospital.entity.Record;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HospitalController {


    private final Environment env;
    @Autowired
    private DoctorLoginService doctorLoginService;

    @PostMapping("/consent_request")
    public Integer generateConsentRequest(@RequestBody ConsentRequest consentRequest){
        String uri = "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/consent_request";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ConsentRequest> request = new HttpEntity<ConsentRequest>(consentRequest, headers);

        ResponseEntity<Integer> response = restTemplate.postForEntity( uri, request , Integer.class);
        return response.getBody();
    }

    @GetMapping("/get_status")
    public List<Object> getConsentRequestStatus(@RequestParam("pId") Integer patientId, @RequestParam("dId") Integer doctorId, @RequestParam("hId") Integer hospitalId){
        String uri = "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/get_status/"+patientId+"/"+doctorId+"/"+hospitalId;
        //IP of Sammati server/API call
        RestTemplate restTemplate = new RestTemplate();
        List<Object> result = restTemplate.getForObject(uri, List.class);
        return result;
    }

    @GetMapping("/get_status_all")
    public List<Object> getConsentRequestStatusAll(@RequestParam("dId") Integer doctorId, @RequestParam("hId") Integer hospitalId){
        String uri = "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/get_status_all/"+doctorId+"/"+hospitalId;
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

    @PostMapping("/addepisode")
    public Integer addEpisode(@RequestParam("patientId") Integer patientId,@RequestParam("episodetype") String episodeType){
        return doctorLoginService.createEpisode(patientId,episodeType);
    }

    @PostMapping("/addvisit")
    public Integer addVisit(@RequestParam("patientId") Integer patientId, @RequestParam("episodeId") Integer episodeId,@RequestParam("doctorId") Integer doctorId){
        return doctorLoginService.createVisit(patientId,episodeId,doctorId);
    }


    @PostMapping("/addrecord")
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

    @GetMapping("/patient_existIn_hospital")
    public Boolean checkPatientExistInHospital(@RequestParam("patientId") Integer patientId ) {
        String uri = "http://"+env.getProperty("app.sammati_server")+":"+env.getProperty("app.sammati_port")+"/patient_existIn_hospital/" + patientId;
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

}
