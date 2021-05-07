package tn.isetsf.bpointage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.isetsf.bpointage.model.MySql.SeanceModel;
import tn.isetsf.bpointage.model.SqlServer.NiveauModelSqlServer;
import tn.isetsf.bpointage.repository.MySql.SeanceRepository;
import tn.isetsf.bpointage.service.SqlServer.NiveauServiceSqlServer;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/group")
public class NiveauController {
    @Autowired
    private NiveauServiceSqlServer niveauServiceSqlServer;
    @Autowired
    private SeanceRepository seanceRepository;
    @GetMapping("/{id}")
    public List<NiveauModelSqlServer> getGroupByEnsi(@PathVariable int id)
    {
        return niveauServiceSqlServer.getNiveauByEnsieg(id);
    }
    @GetMapping("/seance")
    public List<SeanceModel> getSeance()
    {
        return seanceRepository.findAll();
    }
}
