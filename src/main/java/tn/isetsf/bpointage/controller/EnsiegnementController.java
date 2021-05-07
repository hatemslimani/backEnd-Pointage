package tn.isetsf.bpointage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.isetsf.bpointage.model.MySql.SeanceAbsenceModel;
import tn.isetsf.bpointage.model.MySql.SeanceDensiegnement;
import tn.isetsf.bpointage.service.SqlServer.SaisieServiceSqlServer;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/ensiegnement")
public class EnsiegnementController {
    @Autowired
    private SaisieServiceSqlServer saisieServiceSqlServer;
    @GetMapping("/{id}")
    public List<SeanceDensiegnement> getSeanceEnsiegnement(@PathVariable int id)
    {
        return saisieServiceSqlServer.getSaisenceParEnsiegnant(id);
    }
    @GetMapping("/seanceAbsence/{nomEnsiegnant}/{idGroup}")
    public List<SeanceAbsenceModel> getSeanceAbsence(@PathVariable String nomEnsiegnant,@PathVariable int idGroup)
    {
        return saisieServiceSqlServer.getSeanceAbsence(nomEnsiegnant,idGroup);
    }
}
