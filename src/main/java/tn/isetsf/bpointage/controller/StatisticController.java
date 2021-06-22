package tn.isetsf.bpointage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import tn.isetsf.bpointage.model.MySql.AbsenceModel;
import tn.isetsf.bpointage.model.MySql.AnneeUnviModel;
import tn.isetsf.bpointage.model.MySql.DepartementModel;
import tn.isetsf.bpointage.model.SqlServer.SaisieModelSqlServer;
import tn.isetsf.bpointage.model.entity.Absence;
import tn.isetsf.bpointage.model.entity.AdminCardDash;
import tn.isetsf.bpointage.model.entity.StatisticEntity;
import tn.isetsf.bpointage.repository.MySql.AbsenceRepository;
import tn.isetsf.bpointage.repository.SqlServer.SaisieRepositorySqlServer;
import tn.isetsf.bpointage.service.MySql.AbsenceService;
import tn.isetsf.bpointage.service.MySql.CalendarService;
import tn.isetsf.bpointage.service.MySql.DepartementService;
import tn.isetsf.bpointage.service.MySql.UserService;
import tn.isetsf.bpointage.service.SendEmailService;
import tn.isetsf.bpointage.service.SqlServer.EnsiegnantServiceSqlServer;
import tn.isetsf.bpointage.service.SqlServer.SaisieServiceSqlServer;

import javax.mail.MessagingException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8100"})
@RestController
@RequestMapping("/statistic")
public class StatisticController {
    @Autowired
    private DepartementService departementService;
    @Autowired
    private AbsenceService absenceService;
    @Autowired
    private CalendarService calendarService;
    @Autowired
    private UserService userService;
    @Autowired
    private SendEmailService sendEmailService;
    @Autowired
    private EnsiegnantServiceSqlServer ensiegnantServiceSqlServer;
    @Autowired
    private AbsenceRepository absenceRepository;
    @Autowired
    private SaisieServiceSqlServer saisieServiceSqlServer;
    @Autowired
    private SaisieRepositorySqlServer saisieRepositorySqlServer;
    @GetMapping("/nbAbsenceParDep")
    public List<StatisticEntity> getnbAbsenceParDep()
    {
        List<DepartementModel> departements=departementService.getAll();
        List<StatisticEntity> nb=new ArrayList<>();
        for (DepartementModel dep:departements) {
            StatisticEntity n=new StatisticEntity();
            n.setLabel(dep.getNom_dapartement());
            n.addData(absenceService.getNbAbsenceBydep(dep.getId()));
            nb.add(n);
        }
        return nb;
    }
    @GetMapping("/datesAbsences")
    public List<java.sql.Date> getDatesAbsences()
    {
        java.sql.Date toDay= new java.sql.Date(new java.util.Date().getTime());
        AnneeUnviModel anneeUnviModel= calendarService.getAll(toDay);
        if(anneeUnviModel != null)
        {
            return absenceService.getDatesAbsences(anneeUnviModel);
        }
        return null;
    }
    @GetMapping("/nbAbsenceParDate")
    public List<StatisticEntity> getAbsencesByDates()
    {
        java.sql.Date toDay= new java.sql.Date(new java.util.Date().getTime());
        AnneeUnviModel anneeUnviModel= calendarService.getAll(toDay);
        List<StatisticEntity> ls=new ArrayList<>();
        if(anneeUnviModel != null){
        List<java.sql.Date> listDates= absenceService.getDatesAbsences(anneeUnviModel);
        List<DepartementModel> departements=departementService.getAll();
        for (DepartementModel dep:departements) {
            StatisticEntity n=new StatisticEntity();
            n.setLabel(dep.getNom_dapartement());
            for (java.sql.Date date:listDates) {
                n.addData(absenceService.getAllByDateByDep(date,dep.getId()));
            }
            ls.add(n);
        }}
        return ls;
    }
    @GetMapping("/nbAbsenceParDateParDep")
    public List<StatisticEntity> getAbsencesByDatesBydep(@AuthenticationPrincipal UserDetails user)
    {
        java.sql.Date toDay= new java.sql.Date(new java.util.Date().getTime());
        AnneeUnviModel anneeUnviModel= calendarService.getAll(toDay);
        if (anneeUnviModel != null) {
            List<java.sql.Date> listDates = absenceService.getDatesAbsences(anneeUnviModel);
            List<StatisticEntity> ls = new ArrayList<>();
            DepartementModel departement = departementService.getDepartement(userService.getUserByEmail(user.getUsername()).getDepartementt().getId());
            StatisticEntity n = new StatisticEntity();
            n.setLabel(departement.getNom_dapartement());
            for (java.sql.Date date : listDates) {
                n.addData(absenceService.getAllByDateByDep(date, departement.getId()));
            }
            ls.add(n);
            return ls;
        }
        return null;
    }
    @GetMapping("/adminCardDash")
    public AdminCardDash getadminCardDash()
    {
        return departementService.getadminCardDash();
    }
    @GetMapping("/RespCardDash")
    public AdminCardDash getRespCardDash(@AuthenticationPrincipal UserDetails user)
    {
        int idDep=userService.getUserByEmail(user.getUsername()).getDepartementt().getId();
        return departementService.getRespCardDash(idDep);
    }
    @GetMapping("/email")
    public void sendEmail()throws MessagingException
    {
        sendEmailService.sendPassword("hhhhh","ekee");

    }
    @GetMapping("/getRespAllAbsencesByDate/{debut}/{fin}")
    public List<Absence>getRespAllAbsencesByDate(@PathVariable Date debut,@PathVariable Date fin, @AuthenticationPrincipal UserDetails user)
    {
        List<Integer>listEnsei=ensiegnantServiceSqlServer.getEnsiegnantBydepartement(userService.getUserByEmail(user.getUsername()).getDepartementt().getId());
        List<AbsenceModel>ListAbsence=absenceRepository.getAbsenceByDateByDepartement(debut,fin,listEnsei);
        List<Absence>absences=new ArrayList<>();
        for (AbsenceModel absence:ListAbsence) {
            Absence a=new Absence();
            a.setDateAbsence(absence.getDateAbsence());
            a.setMatier(saisieServiceSqlServer.getSeanceById(absence.getIdSeanceEnsiAbsence()).getMatiere().getNom_matiere());
            a.setNomNivean(saisieServiceSqlServer.getSeanceById(absence.getIdSeanceEnsiAbsence()).getNiveau().getNom_niveau());
            a.setNomEnseignant(saisieServiceSqlServer.getSeanceById(absence.getIdSeanceEnsiAbsence()).getEnsiegnant().getNom_Ensi());
            a.setNomSeance(saisieServiceSqlServer.getSeanceById(absence.getIdSeanceEnsiAbsence()).getSeance().getNom_Seance());
            absences.add(a);
        }
        return absences;
    }
    @GetMapping("/getRespAllAbsencesByDateBySeance/{debut}/{fin}/{idSeance}")
    public List<Absence>getRespAllAbsencesByDateBySeance(@PathVariable Date debut,@PathVariable Date fin,@PathVariable int idSeance, @AuthenticationPrincipal UserDetails user)
    {
        List<Integer>listEnsei=ensiegnantServiceSqlServer.getEnsiegnantBydepartement(userService.getUserByEmail(user.getUsername()).getDepartementt().getId());
        List<Integer> listSaise=saisieRepositorySqlServer.getIdSaisiesBySeance(idSeance);
        List<AbsenceModel>ListAbsence=absenceRepository.getAbsenceByDateByDepartementBySeance(debut,fin,listEnsei,listSaise);
        List<Absence>absences=new ArrayList<>();
        for (AbsenceModel absence:ListAbsence) {
            Absence a=new Absence();
            a.setDateAbsence(absence.getDateAbsence());
            a.setMatier(saisieServiceSqlServer.getSeanceById(absence.getIdSeanceEnsiAbsence()).getMatiere().getNom_matiere());
            a.setNomNivean(saisieServiceSqlServer.getSeanceById(absence.getIdSeanceEnsiAbsence()).getNiveau().getNom_niveau());
            a.setNomEnseignant(saisieServiceSqlServer.getSeanceById(absence.getIdSeanceEnsiAbsence()).getEnsiegnant().getNom_Ensi());
            a.setNomSeance(saisieServiceSqlServer.getSeanceById(absence.getIdSeanceEnsiAbsence()).getSeance().getNom_Seance());
            absences.add(a);
        }
        return absences;
    }
}
