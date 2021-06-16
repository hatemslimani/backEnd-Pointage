package tn.isetsf.bpointage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import tn.isetsf.bpointage.model.SqlServer.EnsiegnantModelSqlServer;
import tn.isetsf.bpointage.service.MySql.UserService;
import tn.isetsf.bpointage.service.SqlServer.EnsiegnantServiceSqlServer;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/ensiegnant")
public class EnsiegnantController {
    @Autowired
    private EnsiegnantServiceSqlServer ensiegnantServiceSqlServer;
    @Autowired
    private UserService userService;
    @GetMapping("/{name}")
    public List<EnsiegnantModelSqlServer> getEnsiegnantbyname(@PathVariable String name,@AuthenticationPrincipal UserDetails user)
    {
        return ensiegnantServiceSqlServer.getEnsiegnantByName(name,userService.getUserByEmail(user.getUsername()).getDepartementt().getId());
    }
    @GetMapping("/allByName/{name}")
    public List<EnsiegnantModelSqlServer> getAllEnsiegnantbyname(@PathVariable String name)
    {
        return ensiegnantServiceSqlServer.getAllEnsiegnantByName(name);
    }
}
