package tn.isetsf.bpointage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.isetsf.bpointage.model.MySql.*;
import tn.isetsf.bpointage.model.SqlServer.SaisieModelSqlServer;
import tn.isetsf.bpointage.model.entity.Absence;
import tn.isetsf.bpointage.model.entity.Remplacement;
import tn.isetsf.bpointage.service.MySql.AbsenceService;
import tn.isetsf.bpointage.service.MySql.PreRattrapageService;
import tn.isetsf.bpointage.service.MySql.RattrapageService;
import tn.isetsf.bpointage.service.MySql.SeanceService;
import tn.isetsf.bpointage.service.SqlServer.EnsiegnantServiceSqlServer;
import tn.isetsf.bpointage.service.SqlServer.NiveauServiceSqlServer;
import tn.isetsf.bpointage.service.SqlServer.SaisieServiceSqlServer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/gestionPre")
public class RemplacementController {
    @Autowired
    private SaisieServiceSqlServer saisieServiceSqlServer;
    @Autowired
    private PreRattrapageService preRattrapageService;
    @Autowired
    private EnsiegnantServiceSqlServer ensiegnantServiceSqlServer;
    @Autowired
    private NiveauServiceSqlServer niveauServiceSqlServer;
    @Autowired
    private AbsenceService absenceService;
    @Autowired
    private RattrapageService rattrapageService;
    @Autowired
    private SeanceService seanceService;
    @GetMapping("/")
    public List<Remplacement> getAll()
    {
        //add id departement from user role
        List<Integer> idEnsiegnant=ensiegnantServiceSqlServer.getEnsiegnantBydepartement(6);
        List<PreRattrapageModel> pre=preRattrapageService.getAllByEnsie(idEnsiegnant);
        //test if pre null (aucun preRattrapage)
        List<Remplacement> remplacements=new ArrayList<>();
        for (PreRattrapageModel p:pre) {
            Remplacement r=new Remplacement(p.getCod_rattrapage(),p.getIdEnsiegnant(),p.getDateAbsence(),p.getIdNiveau(),p.getDateRatt(),p.getSalle().getNom_salle(),p.getSeance().getNom_Seance(),p.getAnnee(),p.getSemestre());
            r.setNom_Ensi(ensiegnantServiceSqlServer.getByid(p.getIdEnsiegnant()));
            r.setNom_niveau(niveauServiceSqlServer.getByid(p.getIdNiveau()));
            remplacements.add(r);
        }
        return remplacements;
    }
    @GetMapping("/freeSalle/{dateRatt}/{idSeance}")
    public List<SalleModel> getFreeSalle(@PathVariable String dateRatt ,@PathVariable int idSeance) throws ParseException {
        Date date=new SimpleDateFormat("dd-MM-yyyy").parse(dateRatt);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int idJour=calendar.get(Calendar.DAY_OF_WEEK)+1;
        //test si jour fause est si jour appartient au jour frais
        return saisieServiceSqlServer.getFreeSalle(idJour,idSeance);
    }
    @PostMapping("/addPreRattrapage")
    public void addPreRattrapage(@RequestBody Remplacement pre)
    {
        SaisieModelSqlServer s=saisieServiceSqlServer.getSeanceById(pre.getIdSeanceAbsence());
        if(s!=null) {
            PreRattrapageModel p = new PreRattrapageModel();
            p.setDateRatt(pre.getDateRatt());
            p.setDateAbsence(pre.getDateAbsence());
            p.setIdSeanceAbsence(pre.getIdSeanceAbsence());
            p.setIdNiveau(pre.getIdNiveau());
            p.setSalle(new SalleModel(pre.getIdSalle()));
            p.setSeance(new SeanceModel(pre.getIdSeance()));
            p.setIdEnsiegnant(pre.getIdEnsiegnant());
            p.setAnnee(s.getAnnee1());
            p.setSemestre(s.getSemestre1());
            preRattrapageService.addPre(p);
        }
    }
    @PostMapping("/addRattrapage")
    public void addRattrapage(@RequestBody Remplacement ratt)
    {
        AbsenceModel absence=absenceService.getAbsenceById(ratt.getIdAbsence());
        //test si deja add rattrapage
        if(absence!=null) {
            SaisieModelSqlServer s=saisieServiceSqlServer.getSeanceById(absence.getIdSeanceEnsiAbsence());
            RattrapageModel r = new RattrapageModel();
            r.setDateRatt(ratt.getDateRatt());
            r.setSalle(new SalleModel(ratt.getIdSalle()));
            r.setIdSeanceAbsence(s.getNum());
            r.setSeance(new SeanceModel(ratt.getIdSeance()));
            r.setIdEnsiegnant(ratt.getIdEnsiegnant());
            r.setIdNiveau(s.getNiveau().getCOD_NIVEAU());
            r.setAnnee(s.getAnnee1());
            r.setSemestre(s.getSemestre1());
            r.setAbsence(absenceService.getAbsenceById(ratt.getIdAbsence()));
            rattrapageService.storeRattrapage(r);
        }
    }
    @GetMapping("/getAbcensesNonVerifier/{idEnseignant}")
    public List<Absence> getAbcensesNonVerifier(@PathVariable int idEnseignant)
    {
         List<AbsenceModel> absences= absenceService.getAbcensesNonVerifier(idEnseignant);
         List<Absence>absencesNonV=new ArrayList<>();
         for (AbsenceModel absence:absences)
         {
             SaisieModelSqlServer saisie=saisieServiceSqlServer.getSeanceById(absence.getIdSeanceEnsiAbsence());
             Absence a=new Absence();
             a.setDateAbsence(absence.getDateAbsence());
             a.setMatier(saisie.getMatiere().getNom_matiere());
             a.setNomNivean(saisie.getNiveau().getNom_niveau());
             a.setNomSeance(saisie.getSeance().getNom_Seance());
             a.setIdAbsence(absence.getIdAbsence());
             absencesNonV.add(a);
         }
        return absencesNonV;
    }
    @GetMapping("/allRattrapage")
    public List<Remplacement> getAllRattrapageByEnsie()
    {
        //add id departement from user role
        List<Integer> idEnsiegnant=ensiegnantServiceSqlServer.getEnsiegnantBydepartement(6);
        List<RattrapageModel> rattrapages=rattrapageService.getAllRattrapageByEnsie(idEnsiegnant);
        //test if pre null (aucun preRattrapage)
        List<Remplacement> remplacements=new ArrayList<>();
        for (RattrapageModel rattrapage:rattrapages) {
            Remplacement r=new Remplacement(rattrapage.getIdRattrapage(),rattrapage.getIdEnsiegnant(),rattrapage.getAbsence().getDateAbsence(),rattrapage.getIdNiveau(),rattrapage.getDateRatt(),rattrapage.getSalle().getNom_salle(),rattrapage.getSeance().getNom_Seance(),rattrapage.getAnnee(),rattrapage.getSemestre());
            r.setNom_Ensi(ensiegnantServiceSqlServer.getByid(rattrapage.getIdEnsiegnant()));
            r.setNom_niveau(niveauServiceSqlServer.getByid(rattrapage.getIdNiveau()));
            remplacements.add(r);
        }
        return remplacements;
    }

}
