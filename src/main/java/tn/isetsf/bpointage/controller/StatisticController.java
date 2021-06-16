package tn.isetsf.bpointage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.isetsf.bpointage.model.MySql.AnneeUnviModel;
import tn.isetsf.bpointage.model.MySql.DepartementModel;
import tn.isetsf.bpointage.model.entity.AdminCardDash;
import tn.isetsf.bpointage.model.entity.StatisticEntity;
import tn.isetsf.bpointage.service.MySql.AbsenceService;
import tn.isetsf.bpointage.service.MySql.CalendarService;
import tn.isetsf.bpointage.service.MySql.DepartementService;
import tn.isetsf.bpointage.service.MySql.UserService;
import tn.isetsf.bpointage.service.SendEmailService;

import javax.mail.MessagingException;
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
        return absenceService.getDatesAbsences(anneeUnviModel);
    }
    @GetMapping("/nbAbsenceParDate")
    public List<StatisticEntity> getAbsencesByDates()
    {
        java.sql.Date toDay= new java.sql.Date(new java.util.Date().getTime());
        AnneeUnviModel anneeUnviModel= calendarService.getAll(toDay);
        List<java.sql.Date> listDates= absenceService.getDatesAbsences(anneeUnviModel);
        List<StatisticEntity> ls=new ArrayList<>();
        List<DepartementModel> departements=departementService.getAll();
        for (DepartementModel dep:departements) {
            StatisticEntity n=new StatisticEntity();
            n.setLabel(dep.getNom_dapartement());
            for (java.sql.Date date:listDates) {
                n.addData(absenceService.getAllByDateByDep(date,dep.getId()));
            }
            ls.add(n);
        }
        return ls;
    }
    @GetMapping("/nbAbsenceParDateParDep")
    public List<StatisticEntity> getAbsencesByDatesBydep(@AuthenticationPrincipal UserDetails user)
    {
        java.sql.Date toDay= new java.sql.Date(new java.util.Date().getTime());
        AnneeUnviModel anneeUnviModel= calendarService.getAll(toDay);
        List<java.sql.Date> listDates= absenceService.getDatesAbsences(anneeUnviModel);
        List<StatisticEntity> ls=new ArrayList<>();
        DepartementModel departement=departementService.getDepartement(userService.getUserByEmail(user.getUsername()).getDepartementt().getId());
            StatisticEntity n=new StatisticEntity();
            n.setLabel(departement.getNom_dapartement());
            for (java.sql.Date date:listDates) {
                n.addData(absenceService.getAllByDateByDep(date,departement.getId()));
            }
            ls.add(n);
        return ls;
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
}
