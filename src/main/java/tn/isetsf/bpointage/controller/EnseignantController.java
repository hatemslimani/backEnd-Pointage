package tn.isetsf.bpointage.controller;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.isetsf.bpointage.model.MySql.*;
import tn.isetsf.bpointage.model.SqlServer.NiveauModelSqlServer;
import tn.isetsf.bpointage.model.SqlServer.SaisieModelSqlServer;
import tn.isetsf.bpointage.model.entity.Absence;
import tn.isetsf.bpointage.model.entity.Remplacement;
import tn.isetsf.bpointage.model.MySql.SeanceDenseignement;
import tn.isetsf.bpointage.repository.MySql.SeanceRepository;
import tn.isetsf.bpointage.service.MySql.*;
import tn.isetsf.bpointage.service.SqlServer.EnsiegnantServiceSqlServer;
import tn.isetsf.bpointage.service.SqlServer.NiveauServiceSqlServer;
import tn.isetsf.bpointage.service.SqlServer.SaisieServiceSqlServer;
import tn.isetsf.bpointage.service.MySql.AbsenceService;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8100"})
@RestController
@RequestMapping("/enseignant")
public class EnseignantController {
    @Autowired
    private UserService userService;
    @Autowired
    private PreRattrapageService preRattrapageService;
    @Autowired
    private RattrapageService rattrapageService;
    @Autowired
    private EnsiegnantServiceSqlServer ensiegnantServiceSqlServer;
    @Autowired
    private NiveauServiceSqlServer niveauServiceSqlServer;
    @Autowired
    private SaisieServiceSqlServer saisieServiceSqlServer;
    @Autowired
    private CalendarService calendarService;
    @Autowired
    private AbsenceService absenceService;
    @Autowired
    private SeanceRepository seanceRepository;
    @Autowired
    private NotificationService notificationService;
    @GetMapping("/getPreRattrapages")
    public List<Remplacement> getAllPreRattrapage(@AuthenticationPrincipal UserDetails user)
    {
        List<PreRattrapageModel> pre=preRattrapageService.getAllByIdEnsie(userService.getUserByEmail(user.getUsername()).getIdEnseignant());
        if(!pre.isEmpty()) {
        List<Remplacement> remplacements=new ArrayList<>();
        for (PreRattrapageModel p:pre) {
            Remplacement r = new Remplacement(p.getCod_rattrapage(), p.getIdEnsiegnant(), p.getDateAbsence(), p.getIdNiveau(), p.getDateRatt(), p.getSalle().getNom_salle(), p.getSeance().getNom_Seance(), p.getAnnee(), p.getSemestre());
            r.setNom_Ensi(ensiegnantServiceSqlServer.getByid(p.getIdEnsiegnant()));
            r.setNom_niveau(niveauServiceSqlServer.getByid(p.getIdNiveau()));
            r.setIdSeanceAbsence(p.getIdSeanceAbsence());
            r.setAbNom_Seance(saisieServiceSqlServer.getSeanceById(p.getIdSeanceAbsence()).getSeance().getNom_Seance());
            r.setAbNom_salle(saisieServiceSqlServer.getSeanceById(p.getIdSeanceAbsence()).getSalle().getNom_salle());
            r.setStatus(p.getStatus());
            remplacements.add(r);
        }
        return remplacements; }
        else
        return null;
    }

    @GetMapping("/getRattrapages")
    public List<Remplacement>  getAllRattrapage(@AuthenticationPrincipal UserDetails user)
    {
        List<RattrapageModel> ratt=rattrapageService.getAllByIdEnsie(userService.getUserByEmail(user.getUsername()).getIdEnseignant());
        if (!ratt.isEmpty()) {
            List<Remplacement> remplacements = new ArrayList<>();
            for (RattrapageModel p : ratt) {
                Remplacement r1 = new Remplacement(p.getIdRattrapage(), p.getIdEnsiegnant(), p.getAbsence().getDateAbsence(), p.getIdNiveau(), p.getDateRatt(), p.getSalle().getNom_salle(), p.getSeance().getNom_Seance(), p.getAnnee(), p.getSemestre());
                r1.setNom_Ensi(ensiegnantServiceSqlServer.getByid(p.getIdEnsiegnant()));
                r1.setNom_niveau(niveauServiceSqlServer.getByid(p.getIdNiveau()));
                r1.setIdSeanceAbsence(p.getIdSeanceAbsence());
                r1.setAbNom_Seance(saisieServiceSqlServer.getSeanceById(p.getIdSeanceAbsence()).getSeance().getNom_Seance());
                r1.setAbNom_salle(saisieServiceSqlServer.getSeanceById(p.getIdSeanceAbsence()).getSalle().getNom_salle());
                r1.setStatus(p.getStatus());
                remplacements.add(r1);
            }
            return remplacements;
        }else
        return null;
    }



    @PostMapping("/addPreRattrapage")
    public void addPreRattrapage(@RequestBody Remplacement pre, @AuthenticationPrincipal UserDetails user1) {
        SaisieModelSqlServer s=saisieServiceSqlServer.getSeanceById(pre.getIdSeanceAbsence());
        if(s!=null) {
            PreRattrapageModel p = new PreRattrapageModel();
            p.setDateRatt(pre.getDateRatt());
            p.setDateAbsence(pre.getDateAbsence());
            p.setIdSeanceAbsence(pre.getIdSeanceAbsence());
            p.setIdNiveau(s.getNiveau().getCOD_NIVEAU());
            p.setSalle(new SalleModel(pre.getIdSalle()));
            p.setSeance(new SeanceModel(pre.getIdSeance()));
            p.setIdEnsiegnant(s.getEnsiegnant().getCOD_Enseig());
            p.setAnnee(s.getAnnee1());
            p.setSemestre(s.getSemestre1());
            User user=userService.getResponsableByDepartement(ensiegnantServiceSqlServer.getEnsiegnantById(userService.getUserByEmail(user1.getUsername()).getIdEnseignant()).getDepartement().getCOD_dep());
            if (user != null) {
                NotificationModel notificationModel = new NotificationModel();
                notificationModel.setTitle("Demande de remplacement");
                notificationModel.setMsg("L'enseignant "+userService.getUserByEmail(user1.getUsername()).getNom()+" demande un pre-rattrapage");
                notificationModel.setIdReciver(user.getId());
                notificationModel.setVu(true);
                notificationService.save(notificationModel);
            }
        }
    }

    @PostMapping("/addRattrapage")
    public void addRattrapage(@RequestBody Remplacement ratt, @AuthenticationPrincipal UserDetails user1)
    {
        AbsenceModel absence=absenceService.getAbsenceById(ratt.getIdAbsence());
        //test si deja add rattrapage
        if(absence!=null) {
            SaisieModelSqlServer s=saisieServiceSqlServer.getSeanceById(absence.getIdSeanceEnsiAbsence());
            RattrapageModel r = new RattrapageModel();
            r.setDateRatt(ratt.getDateRatt());
            r.setSalle(new SalleModel(ratt.getIdSalle()));
            r.setIdSeanceAbsence(absence.getIdSeanceEnsiAbsence());
            r.setSeance(new SeanceModel(ratt.getIdSeance()));
            r.setIdEnsiegnant(absence.getIdEnsiegnant());
            r.setIdNiveau(absence.getIdAbsence());
            r.setAnnee(absence.getAnnee());
            r.setSemestre(absence.getSemestre());
            r.setAbsence(absenceService.getAbsenceById(ratt.getIdAbsence()));
            User user=userService.getResponsableByDepartement(ensiegnantServiceSqlServer.getEnsiegnantById(userService.getUserByEmail(user1.getUsername()).getIdEnseignant()).getDepartement().getCOD_dep());
            if (user != null) {
                NotificationModel notificationModel = new NotificationModel();
                notificationModel.setTitle("Demande de remplacement");
                notificationModel.setMsg("L'enseignant "+userService.getUserByEmail(user1.getUsername()).getNom()+" demande un rattrapage");
                notificationModel.setIdReciver(user.getId());
                notificationModel.setVu(true);
                notificationService.save(notificationModel);
            }
        }
    }


    @GetMapping("/getNiveau")
    public List<NiveauModelSqlServer> getNiveau(@AuthenticationPrincipal UserDetails user)
    {
        return niveauServiceSqlServer.getNiveauByEnsieg(userService.getUserByEmail(user.getUsername()).getIdEnseignant());
    }

    @DeleteMapping("/deleteEnseignantPreRatt/{id}")
    public void deletePreRattrapage(@PathVariable int id)
    {
        PreRattrapageModel p=preRattrapageService.getById(id);
        if (p != null)
        {
            if (p.getStatus()==1 && p.isEnsiegnee()==false) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " Suppression est impossible " + "\n" +
                        " Votre demande de pré-rattrapage est validée par le responsable");
            }
            if (p.isEnsiegnee()==true)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Supprission impossible " +
                        " ce pré-rattrapage est déja enseigné");
            this.preRattrapageService.deleteEnseignantPreRatt(id);
        }
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Supprission impossible " +
                    " pré-rattrapage n'existe pas");
    }

    @DeleteMapping("/deleteEnseignantRatt/{id}")
    public void deleteRattrappage(@PathVariable int id){
        RattrapageModel r =rattrapageService.getById(id);
        if (r != null)
        {
            if (r.getStatus()==1 && r.isEnsiegnee()==false) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " Suppression est impossible " + "\n" +
                        " Votre demande de rattrapage est validée par le responsable");
            }
            if (r.isEnsiegnee()==true)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Supprission impossible " +
                        " ce rattrapage est déja enseigné");
            this.rattrapageService.deleteEnseignantRatt(id);
        }
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Supprission impossible " +
                    " rattrapage n'existe pas");
    }


    @GetMapping("/SeanceDenseignement/{dateAbsence}")
    public List<SeanceDenseignement> getSeanceDenseignement(@PathVariable Date dateAbsence,@AuthenticationPrincipal UserDetails user)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateAbsence);
        if ( cal.get(Calendar.DAY_OF_WEEK)-1==0 )
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Jour non valide");
        }
        int year=cal.get(Calendar.YEAR);
        AnneeUnviModel anneeUniv=calendarService.getAll(dateAbsence);
        if (anneeUniv==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Jour non valide");
        }
        int semestre=0;
        Interval interval1=new Interval(new DateTime(anneeUniv.getStartSemstre1()),new DateTime(anneeUniv.getEndSemestre1()));
        Interval interval2=new Interval(new DateTime(anneeUniv.getStartSemstre2()),new DateTime(anneeUniv.getEndSemestre2()));
        if (interval1.contains(new DateTime(dateAbsence)))
        {
            semestre=1;
        }
        if (interval2.contains(new DateTime(dateAbsence)))
        {
            semestre=2;
        }
        if ( semestre==0 )
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Jour non valide");
        }
        List<SaisieModelSqlServer> saisie= saisieServiceSqlServer.getSeanceDenseignement(cal.get(Calendar.DAY_OF_WEEK)-1,userService.getUserByEmail(user.getUsername()).getIdEnseignant(),year,semestre);
        List<SeanceDenseignement> seanceDenseignements=new ArrayList<>();
        for (SaisieModelSqlServer s:saisie) {
            SeanceDenseignement seance=new SeanceDenseignement();
            seance.setIdSeanceEnseignement(s.getNum());
            seance.setNomSeance(s.getSeance().getNom_Seance());
            seance.setMatier(s.getMatiere().getNom_matiere());
            seance.setNomNivean(s.getNiveau().getNom_niveau());
            seanceDenseignements.add(seance);
        }
        return seanceDenseignements;
    }

    @GetMapping("/getSeanceAbsence")
    public List<Absence> SeanceAbsence (@AuthenticationPrincipal UserDetails user){
        java.sql.Date toDay=new java.sql.Date(new java.util.Date().getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(toDay);
        int year=cal.get(Calendar.YEAR);
        AnneeUnviModel anneeUniv=calendarService.getAll(toDay);
        int semestre=0;
        Interval interval1=new Interval(new DateTime(anneeUniv.getStartSemstre1()),new DateTime(anneeUniv.getEndSemestre1()));
        Interval interval2=new Interval(new DateTime(anneeUniv.getStartSemstre2()),new DateTime(anneeUniv.getEndSemestre2()));
        if (interval1.contains(new DateTime(toDay)))
        {
            semestre=1;
        }
        if (interval2.contains(new DateTime(toDay)))
        {
            semestre=2;
        }
        if ( semestre==0 )
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Jour non valide");
        }
        System.out.println("semestre : "+ semestre + "   annee ; "+year);
        List<AbsenceModel> absence= absenceService.getAbsenceByIdEnseignant(year,semestre,userService.getUserByEmail(user.getUsername()).getIdEnseignant());
        List<Absence> ListAbsence=new ArrayList<>();
        for (AbsenceModel s:absence) {
            Absence absence1=new Absence();
            absence1.setIdAbsence(s.getIdAbsence());
            absence1.setDateAbsence(s.getDateAbsence());
            SaisieModelSqlServer saisie=saisieServiceSqlServer.getSeanceById(s.getIdSeanceEnsiAbsence());
            absence1.setNomNivean(saisie.getNiveau().getNom_niveau());
            absence1.setMatier(saisie.getMatiere().getNom_matiere());
            absence1.setNomSeance(saisie.getSeance().getNom_Seance());
            ListAbsence.add(absence1);
        }
        return ListAbsence;
    }
    @GetMapping("/seance")
    public List<SeanceModel> getSeance()
    {
        return seanceRepository.findAll();
    }
    @GetMapping("/getAllAbsences")
    public List<Absence> getAllAbsences(@AuthenticationPrincipal UserDetails user)
    {
        java.sql.Date toDay=new java.sql.Date(new java.util.Date().getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(toDay);
        int year=cal.get(Calendar.YEAR);
        AnneeUnviModel anneeUniv=calendarService.getAll(toDay);
        int semestre=0;
        Interval interval1=new Interval(new DateTime(anneeUniv.getStartSemstre1()),new DateTime(anneeUniv.getEndSemestre1()));
        Interval interval2=new Interval(new DateTime(anneeUniv.getStartSemstre2()),new DateTime(anneeUniv.getEndSemestre2()));
        if (interval1.contains(new DateTime(toDay)))
        {
            semestre=1;
        }
        if (interval2.contains(new DateTime(toDay)))
        {
            semestre=2;
        }
        if ( semestre==0 )
        {
            return null;
        }
        System.out.println("semestre : "+ semestre + "   annee ; "+year);
        List<AbsenceModel> absenceModelList=absenceService.getAbsenceByIdEnseignant(year,semestre,userService.getUserByEmail(user.getUsername()).getIdEnseignant());
        List<Absence>absences=new ArrayList<>();
        for (AbsenceModel a:absenceModelList) {
            SaisieModelSqlServer saisie=saisieServiceSqlServer.getSeanceById(a.getIdSeanceEnsiAbsence());
            Absence absence=new Absence(a.getIdAbsence(),a.getDateAbsence(),saisie.getSeance().getNom_Seance(),
                    saisie.getMatiere().getNom_matiere(),saisie.getNiveau().getNom_niveau());
            absences.add(absence);
        }
        return absences;
    }
    @GetMapping("/getNewNotification")
    public List<NotificationModel>getNewNotification(@AuthenticationPrincipal UserDetails user)
    {
        List<NotificationModel> notificationList=notificationService.getNewNotification(userService.getUserByEmail(user.getUsername()).getId());
        return notificationList;
    }
    @GetMapping("/getOldNotification")
    public List<NotificationModel>getOldNotification(@AuthenticationPrincipal UserDetails user)
    {
        List<NotificationModel> notificationList=notificationService.getOldNotification(userService.getUserByEmail(user.getUsername()).getId());
        return notificationList;
    }
    @PostMapping("/MarkVue")
    private void markerVu(@RequestBody List<NotificationModel> notificationList) {
        for (NotificationModel notification:notificationList) {
            notification.setVu(false);
            notificationService.save(notification);
        }
    }


}

