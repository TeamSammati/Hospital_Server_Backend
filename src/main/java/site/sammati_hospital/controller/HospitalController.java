package site.sammati_hospital.controller;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import site.sammati_hospital.dto.ConsentRequest;

import java.util.List;

@RestController
public class HospitalController {

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

    @GetMapping("/get_status/{pid}/{did}/{hid}")
    public List<Object> getConsentRequestStatus(@PathVariable("pid") Integer patientId, @PathVariable("did") Integer doctorId, @PathVariable("hid") Integer hospitalId){
        String uri = "http://172.16.133.184:6969/get_status/"+patientId+"/"+doctorId+"/"+hospitalId;
        //IP of Sammati server/API call
        RestTemplate restTemplate = new RestTemplate();
        List<Object> result = restTemplate.getForObject(uri, List.class);
        return result;
    }
}
