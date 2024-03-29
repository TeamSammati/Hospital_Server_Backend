package site.sammati_hospital.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import static site.sammati_hospital.service.PatientAuthService.*;

@RestController
@CrossOrigin("*")
public class HandleDataController {

    @GetMapping("/authorize-patient")
    @PreAuthorize("hasAuthority('SAMMATI')")
    public String authorizePatient(@RequestParam("patientId") Integer pid) {
        clearOTPFromCache(pid.toString());
        String otp = genString(pid.toString());
        System.out.println(otp);
        return otp;
    }

    @GetMapping("/validate-patient")
    @PreAuthorize("hasAuthority('SAMMATI')")
    public boolean validatePatient(@RequestParam("patientId") Integer pid, @RequestParam("str") String str) {
        String pto = getStrByPID(pid.toString());
        if(pto==null)
            return false;
        return str.equals(pto);
    }

}
