package site.sammati_hospital.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static site.sammati_hospital.service.PatientAuthService.*;

@RestController
public class HandleDataController {

    @PostMapping("/authorize-patient")
    public Integer authorizePatient(@RequestParam("patientId") Integer pid) {
        clearOTPFromCache(pid.toString());
        Integer otp = generateOTP(pid.toString());
        System.out.println(otp);
        return otp;
    }

    @PostMapping("/validate-patient")
    public boolean validateOTP(@RequestParam("patientId") Integer pid, @RequestParam("otp") String otp) {
        String pto = getOPTByKey(pid.toString());
        if(pto==null)
            return false;
        return otp.equals(pto);
    }
}
