package tn.isetsf.bpointage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.isetsf.bpointage.model.SqlServer.EnsiegnantModelSqlServer;
import tn.isetsf.bpointage.service.SqlServer.EnsiegnantServiceSqlServer;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/ensiegnant")
public class EnsiegnantController {
    @Autowired
    private EnsiegnantServiceSqlServer ensiegnantServiceSqlServer;
    @GetMapping("/{name}")
    public List<EnsiegnantModelSqlServer> getEnsiegnantbyname(@PathVariable String name)
    {
        return ensiegnantServiceSqlServer.getEnsiegnantByName(name);
    }
}
