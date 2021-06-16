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
import tn.isetsf.bpointage.service.SqlServer.MatierServiceSqlService;
import tn.isetsf.bpointage.service.SqlServer.NiveauServiceSqlServer;
import tn.isetsf.bpointage.service.SqlServer.SaisieServiceSqlServer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/remplacement")
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
    private MatierServiceSqlService matierServiceSqlService;
    @Autowired
    private SeanceRepository seanceRepository;
    @Autowired
    private JourService jourService;
    @Autowired
    private CalendarService calendarService;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;
    java.sql.Date toDay;
    Calendar calendar;
    int idJour;
    JourModel jour;
    AnneeUnviModel anneeUniv;
    List<EventModel> events;
    int semestre=0;
    int year;
    public void init() {
        this.toDay= new java.sql.Date(new java.util.Date().getTime());
        this.calendar = Calendar.getInstance();
        calendar.setTime(toDay);
        this.idJour=calendar.get(Calendar.DAY_OF_WEEK)-1;
        this.year=calendar.get(Calendar.YEAR);
        this.jour =jourService.getJourById(idJour);
        this.anneeUniv=calendarService.getAll(toDay);
        this.events=calendarService.getEventsByDate(toDay);
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
    }
    @GetMapping("/")
    public List<Remplacement> getAll(@AuthenticationPrincipal UserDetails user)
    {
        init();
        List<Integer> idEnsiegnant=ensiegnantServiceSqlServer.getEnsiegnantBydepartement(userService.getUserByEmail(user.getUsername()).getDepartementt().getId());
        List<PreRattrapageModel> pre=preRattrapageService.getAllByEnsie(idEnsiegnant,this.year,this.semestre);
        List<Remplacement> remplacements=new ArrayList<>();
        for (PreRattrapageModel p:pre) {
            Remplacement r=new Remplacement(p.getCod_rattrapage(),p.getIdEnsiegnant(),p.getDateAbsence(),p.getIdNiveau(),p.getDateRatt(),p.getSalle().getNom_salle(),p.getSeance().getNom_Seance(),p.getAnnee(),p.getSemestre());
            r.setNom_Ensi(ensiegnantServiceSqlServer.getByid(p.getIdEnsiegnant()));
            r.setStatus(p.getStatus());
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
            p.setStatus(1);
            p.setAnnee(s.getAnnee1());
            p.setSemestre(s.getSemestre1());
            preRattrapageService.addPre(p);
        }
    }
    @GetMapping("/deletePreRattrapage/{idPre}")
    public void deletePreRattrapage(@PathVariable int idPre)
    {
        PreRattrapageModel preRattrapage=preRattrapageService.getById(idPre);
        if (preRattrapage != null)
        {
            if (preRattrapage.isEnsiegnee())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Deja Enseignee");
            }
            preRattrapageService.deletePreRattrapage(preRattrapage);
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"N'exist pas");
        }
    }
    @GetMapping("/deleteRattrapage/{idRatt}")
    public void deleteRattrapage(@PathVariable int idRatt)
    {
        RattrapageModel rattrapage=rattrapageService.getById(idRatt);
        if (rattrapage != null)
        {
            if (rattrapage.isEnsiegnee())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Deja Enseignee");
            }
            rattrapageService.deletePreRattrapage(rattrapage);
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"N'exist pas");
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
            r.setStatus(1);
            r.setEnsiegnee(r.isEnsiegnee());
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
    public List<Remplacement> getAllRattrapageByEnsie(@AuthenticationPrincipal UserDetails user)
    {
        init();
        List<Integer> idEnsiegnant=ensiegnantServiceSqlServer.getEnsiegnantBydepartement(userService.getUserByEmail(user.getUsername()).getDepartementt().getId());
        List<RattrapageModel> rattrapages=rattrapageService.getAllRattrapageByEnsie(idEnsiegnant,this.year,this.semestre);
        List<Remplacement> remplacements=new ArrayList<>();
        for (RattrapageModel rattrapage:rattrapages) {
            Remplacement r=new Remplacement(rattrapage.getIdRattrapage(),rattrapage.getIdEnsiegnant(),rattrapage.getAbsence().getDateAbsence(),rattrapage.getIdNiveau(),rattrapage.getDateRatt(),rattrapage.getSalle().getNom_salle(),rattrapage.getSeance().getNom_Seance(),rattrapage.getAnnee(),rattrapage.getSemestre());
            r.setNom_Ensi(ensiegnantServiceSqlServer.getByid(rattrapage.getIdEnsiegnant()));
            r.setStatus(rattrapage.getStatus());
            r.setEnsiegnee(rattrapage.isEnsiegnee());
            r.setNom_niveau(niveauServiceSqlServer.getByid(rattrapage.getIdNiveau()));
            remplacements.add(r);
        }
        return remplacements;
    }
    @GetMapping("group/{id}")
    public List<NiveauModelSqlServer> getGroupByEnsi(@PathVariable int id)
    {
        return niveauServiceSqlServer.getNiveauByEnsieg(id);
    }
    @GetMapping("group/seance")
    public List<SeanceModel> getSeance()
    {
        return seanceRepository.findAll();
    }
    @GetMapping("ensiegnement/{id}")
    public List<SeanceDenseignement> getSeanceEnsiegnement(@PathVariable int id)
    {
        return saisieServiceSqlServer.getSaisenceParEnsiegnant(id);
    }
    @GetMapping("ensiegnement/seanceAbsence/{nomEnsiegnant}/{idGroup}")
    public List<SeanceAbsenceModel> getSeanceAbsence(@PathVariable String nomEnsiegnant,@PathVariable int idGroup)
    {
        return saisieServiceSqlServer.getSeanceAbsence(nomEnsiegnant,idGroup);
    }
    @GetMapping("/getPreRattrapageById/{idPre}")
    public Remplacement getPreRattrapageById(@PathVariable int idPre)
    {
        PreRattrapageModel p=preRattrapageService.getById(idPre);
        if (p==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Pre-rattrapage introuvable");
        }
        Remplacement r=new Remplacement(p.getCod_rattrapage(),p.getIdEnsiegnant(),p.getDateAbsence(),p.getIdNiveau(),p.getDateRatt(),p.getSalle().getNom_salle(),p.getSeance().getNom_Seance(),p.getAnnee(),p.getSemestre());
        r.setNom_Ensi(ensiegnantServiceSqlServer.getByid(p.getIdEnsiegnant()));
        r.setStatus(p.getStatus());
        r.setMatiere(saisieServiceSqlServer.getSeanceById(p.getIdSeanceAbsence()).getMatiere().getNom_matiere());
        r.setNom_niveau(niveauServiceSqlServer.getByid(p.getIdNiveau()));
        r.setAbNom_salle(saisieServiceSqlServer.getSeanceById(p.getIdSeanceAbsence()).getSalle().getNom_salle());
        r.setAbNom_Seance(saisieServiceSqlServer.getSeanceById(p.getIdSeanceAbsence()).getNiveau().getNom_niveau());
        return r;
    }
    @GetMapping("/getRattrapageById/{idPre}")
    public Remplacement getRattrapageById(@PathVariable int idPre)
    {
        RattrapageModel p=rattrapageService.getById(idPre);
        if (p==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Pre-rattrapage introuvable");
        }
        Remplacement r=new Remplacement(p.getIdRattrapage(),p.getIdEnsiegnant(),p.getAbsence().getDateAbsence(),p.getIdNiveau(),p.getDateRatt(),p.getSalle().getNom_salle(),p.getSeance().getNom_Seance(),p.getAnnee(),p.getSemestre());
        r.setNom_Ensi(ensiegnantServiceSqlServer.getByid(p.getIdEnsiegnant()));
        r.setStatus(p.getStatus());
        r.setMatiere(saisieServiceSqlServer.getSeanceById(p.getAbsence().getIdSeanceEnsiAbsence()).getMatiere().getNom_matiere());
        r.setNom_niveau(niveauServiceSqlServer.getByid(p.getIdNiveau()));
        r.setAbNom_salle(saisieServiceSqlServer.getSeanceById(p.getAbsence().getIdSeanceEnsiAbsence()).getSalle().getNom_salle());
        r.setAbNom_Seance(saisieServiceSqlServer.getSeanceById(p.getAbsence().getIdSeanceEnsiAbsence()).getNiveau().getNom_niveau());
        return r;
    }
    @PostMapping("/ChangeStatusPreRattrapage")
    public void ChangeStatusPreRattrapage(@RequestBody Remplacement r)
    {
        PreRattrapageModel preRattrapageModel=preRattrapageService.getById(r.getCod_rattrapage());
        if (preRattrapageModel==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Pre-rattrapage introuvable");
        }
        User user=userService.getEnseignant(preRattrapageModel.getIdEnsiegnant());
        if (user != null)
        {
        if (r.getStatus()==1)
        {
            NotificationModel notificationModel=new NotificationModel();
            notificationModel.setTitle("D'apérs l'Administration de departement "+ensiegnantServiceSqlServer.getEnsiegnantById(preRattrapageModel.getIdEnsiegnant()).getDepartement().getNom_dep());
            notificationModel.setMsg("Bonjour Mr/Mme "+user.getNom()+" votre Remplacement a été accepté");
            notificationModel.setIdReciver(user.getId());
            notificationModel.setVu(true);
            notificationService.save(notificationModel);
        }else
        {
            NotificationModel notificationModel=new NotificationModel();
            notificationModel.setTitle("D'apérs l'Administration de departement "+ensiegnantServiceSqlServer.getEnsiegnantById(preRattrapageModel.getIdEnsiegnant()).getDepartement().getNom_dep());
            notificationModel.setMsg("Bonjour Mr/Mme "+user.getNom()+" votre Remplacement a été refusé aa couse de certaines contraintes");
            notificationModel.setIdReciver(user.getId());
            notificationModel.setVu(true);
            notificationService.save(notificationModel);
        }}
        preRattrapageModel.setStatus(r.getStatus());
        preRattrapageService.savePreRattrapage(preRattrapageModel);
    }
    @PostMapping("/ChangeStatusRattrapage")
    public void ChangeStatusRattrapage(@RequestBody Remplacement r)
    {
        RattrapageModel rattrapageModel=rattrapageService.getById(r.getCod_rattrapage());
        if (rattrapageModel==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Pre-rattrapage introuvable");
        }
        User user=userService.getEnseignant(rattrapageModel.getIdEnsiegnant());
        if (user != null)
        {
            if (r.getStatus()==1)
            {
                NotificationModel notificationModel=new NotificationModel();
                notificationModel.setTitle("D'apérs l'Administration de departement "+ensiegnantServiceSqlServer.getEnsiegnantById(rattrapageModel.getIdEnsiegnant()).getDepartement().getNom_dep());
                notificationModel.setMsg("Bonjour Mr/Mme "+user.getNom()+" votre Remplacement a été accepté");
                notificationModel.setIdReciver(user.getId());
                notificationModel.setVu(true);
                notificationService.save(notificationModel);
            }else
            {
                NotificationModel notificationModel=new NotificationModel();
                notificationModel.setTitle("D'apérs l'Administration de departement "+ensiegnantServiceSqlServer.getEnsiegnantById(rattrapageModel.getIdEnsiegnant()).getDepartement().getNom_dep());
                notificationModel.setMsg("Bonjour Mr/Mme "+user.getNom()+" votre Remplacement a été refusé a cause de certaines contraintes");
                notificationModel.setIdReciver(user.getId());
                notificationModel.setVu(true);
                notificationService.save(notificationModel);
            }}
        rattrapageModel.setStatus(r.getStatus());
        rattrapageService.saveRattrapage(rattrapageModel);
    }
}
