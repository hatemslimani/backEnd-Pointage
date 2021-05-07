package tn.isetsf.bpointage.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8100"})
@RestController
@RequestMapping("/testt")
//@EnableGlobalMethodSecurity(prePostEnabled=true)
public class TestController {
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/")
    public String  admin()
    {
        return "admin";
    }
    @GetMapping("/Ensiegnant")
    public String  Ensiegnant()
    {
        return "Ensiegnant";
    }
    //@PreAuthorize("hasRole('ROLE_CONTROLLER')")
    @GetMapping("/controller")
    public String  controller()
    {
        return "controller";
    }
}
