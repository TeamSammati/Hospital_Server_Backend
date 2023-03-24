package site.sammati_hospital.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import site.sammati_hospital.Service.DoctorLoginService;
import site.sammati_hospital.dto.ConsentRequest;
import site.sammati_hospital.dto.Credentials;
import site.sammati_hospital.entity.Doctor;

import java.util.List;

@RestController
public class HospitalController {

    @Autowired
    private DoctorLoginService doctorLoginService;

    @PostMapping("/consent_request")
    public Integer generateConsentRequest(@RequestBody ConsentRequest consentRequest){
        String uri = "http://172.16.133.184:6969/consent_request";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ConsentRequest> request = new HttpEntity<ConsentRequest>(consentRequest, headers);

        ResponseEntity<Integer> response = restTemplate.postForEntity( uri, request , Integer.class);
        return response.getBody();
    }

    @GetMapping("/get_status")
    public List<Object> getConsentRequestStatus(@RequestParam("pId") Integer patientId, @RequestParam("dId") Integer doctorId, @RequestParam("hId") Integer hospitalId){
        String uri = "http://172.16.133.184:6969/get_status/"+patientId+"/"+doctorId+"/"+hospitalId;
        //IP of Sammati server/API call
        RestTemplate restTemplate = new RestTemplate();
        List<Object> result = restTemplate.getForObject(uri, List.class);
        return result;
    }

    @GetMapping("/get_status_all")
    public List<Object> getConsentRequestStatusAll(@RequestParam("dId") Integer doctorId, @RequestParam("hId") Integer hospitalId){
        String uri = "http://172.16.133.184:6969/get_status_all/"+doctorId+"/"+hospitalId;
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
    public void addEpisode(@RequestParam("patientId") Integer patientId,@RequestParam("episodetype") String episodeType){
        doctorLoginService.createEpisode(patientId,episodeType);
    }

    @PostMapping("/addvisit")
    public void addVisit(@RequestParam("patientId") Integer patientId, @RequestParam("episodeId") Integer episodeId,@RequestParam("doctorId") Integer doctorId){
        doctorLoginService.createVisit(patientId,episodeId,doctorId);
    }
}
