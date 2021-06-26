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

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
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
        this.year=calendar.get(Calendar.YEAR);
        this.jour =jourService.getJourById(idJour);
        this.anneeUniv=calendarService.getAll(toDay);
        this.events=calendarService.getEventsByDate(toDay);
            if (anneeUniv != null) {
                Interval interval1 = new Interval(new DateTime(anneeUniv.getStartSemstre1()), new DateTime(anneeUniv.getEndSemestre1()));
                Interval interval2 = new Interval(new DateTime(anneeUniv.getStartSemstre2()), new DateTime(anneeUniv.getEndSemestre2()));
                if (interval1.contains(new DateTime(toDay))) {
                    semestre = 1;
                }
                if (interval2.contains(new DateTime(toDay))) {
                    semestre = 2;
                }
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
    public List<SalleModel> getFreeSalle(@PathVariable java.sql.Date dateRatt , @PathVariable int idSeance) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateRatt);
        int idJour=calendar.get(Calendar.DAY_OF_WEEK)-1;
        if (jourService.getJourById(idJour)==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Date de remplacement invalide");
        }
        return saisieServiceSqlServer.getFreeSalle(idJour,idSeance);
    }
    @GetMapping("/getFreeSallePre/{dateRatt}/{idSeance}")
    public List<SalleModel> getFreeSallePre(@PathVariable java.sql.Date dateRatt , @PathVariable int idSeance) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateRatt);
        int idJour=calendar.get(Calendar.DAY_OF_WEEK)-1;
        if (jourService.getJourById(idJour)==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Date de remplacement invalide");
        }
        List<Integer>idSallePre=preRattrapageService.getSallePre(dateRatt,idSeance);
        List<Integer>idSalleRatt=rattrapageService.getSalleRatt(dateRatt,idSeance);
        idSallePre.add(0);idSalleRatt.add(0);
        List<Integer>idSeanceEnnseignament=seanceRepository.getIdseanceensie(idSeance);
        return saisieServiceSqlServer.getFreeSallePre(idJour,idSeanceEnnseignament,idSallePre,idSalleRatt);
    }
    @PostMapping("/addPreRattrapage")
    public void addPreRattrapage(@RequestBody Remplacement pre)
    {
        SaisieModelSqlServer s=saisieServiceSqlServer.getSeanceById(pre.getIdSeanceAbsence());
        PreRattrapageModel preRattrapageModel=preRattrapageService.getBySeanceDabsenceByDate(pre.getIdSeanceAbsence(),pre.getDateAbsence());
        if (preRattrapageModel!=null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Un pré-rattrapage est planifié pour cette séance d'enseignement");
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
            throw new ResponseStatusException(HttpStatus.OK,"Pre-rattrapage supprimé");
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
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Rattrapage déja Enseigné");
            }
            rattrapageService.deletePreRattrapage(rattrapage);
            throw new ResponseStatusException(HttpStatus.OK,"Rattrapage supprimé avec succès");
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Rattrapage n'exist pas");
        }
    }

    @PostMapping("/addRattrapage")
    public void addRattrapage(@RequestBody Remplacement ratt)
    {
        AbsenceModel absence=absenceService.getAbsenceById(ratt.getIdAbsence());
        //test si deja add rattrapage "Un rattrapage est planifié pour cette absence"
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
        init();
        return niveauServiceSqlServer.getNiveauByEnsieg(id,this.year,this.semestre);
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
    @GetMapping("ensiegnement/seanceAbsence/{nomEnsiegnant}/{idGroup}/{dateAbsence}")
    public List<SeanceAbsenceModel> getSeanceAbsence(@PathVariable String nomEnsiegnant, @PathVariable int idGroup, @PathVariable java.sql.Date dateAbsence)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateAbsence);
        init();
        int idJour=calendar.get(Calendar.DAY_OF_WEEK)-1;
        if (jourService.getJourById(idJour)==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Date d'absence invalide");
        }
        return saisieServiceSqlServer.getSeanceAbsence(nomEnsiegnant,idGroup,idJour,this.year,this.semestre);
    }
    @GetMapping("/getPreRattrapageById/{idPre}")
    public Remplacement getPreRattrapageById(@PathVariable int idPre)
    {
        PreRattrapageModel p=preRattrapageService.getById(idPre);
        if (p==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Pre-rattrapage n'existe pas");
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Pre-rattrapage n'existe pas");
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Pre-rattrapage n'existe pas");
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
        if (r.getStatus()==1)throw new ResponseStatusException(HttpStatus.OK,"Pre-rattrapage accepté");
        else throw new ResponseStatusException(HttpStatus.OK,"Pre-rattrapage refusé");
    }
    @PostMapping("/ChangeStatusRattrapage")
    public void ChangeStatusRattrapage(@RequestBody Remplacement r)
    {
        RattrapageModel rattrapageModel=rattrapageService.getById(r.getCod_rattrapage());
        if (rattrapageModel==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Pre-rattrapage n'existe pas");
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
                notificationModel.setMsg("Bonjour Mr/Mme "+user.getNom()+" votre Remplacement a été refusé pour certaines contraintes");
                notificationModel.setIdReciver(user.getId());
                notificationModel.setVu(true);
                notificationService.save(notificationModel);
            }}
        rattrapageModel.setStatus(r.getStatus());
        rattrapageService.saveRattrapage(rattrapageModel);
        if (r.getStatus()==1)throw new ResponseStatusException(HttpStatus.OK,"Rattrapage accepté");
        else throw new ResponseStatusException(HttpStatus.OK,"Rattrapage refusé");
    }
    @GetMapping("/getSeancesPossibles/{dateRatt}/{idEnseignant}/{idGroup}/{idSeanceAbsence}")
    public List<SeanceModel> getSeancesPossibles(@PathVariable java.sql.Date dateRatt, @PathVariable int idEnseignant,@PathVariable int idGroup,@PathVariable int idSeanceAbsence)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateRatt);
        int idJour=calendar.get(Calendar.DAY_OF_WEEK)-1;
        if (jourService.getJourById(idJour)==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Date de remplacement invalide");
        }
        List<Integer> idSeanceGroupe=saisieServiceSqlServer.getIdSeanceGroupe(idJour,idGroup);
        List<Integer> idSeanceEnseignant=saisieServiceSqlServer.getIdSeanceEnseignant(idJour,idEnseignant);
        List<Integer> idSeancePreGroupe=preRattrapageService.getidSeancePreGroupe(dateRatt,idGroup);
        List<Integer> idSeanceRattGroupe=rattrapageService.getidSeanceRattGroupe(dateRatt,idGroup);
        List<Integer> idSeancePreEnseignant=preRattrapageService.getidSeancePreEnseignant(dateRatt,idGroup);
        List<Integer> idSeanceRattEnseigant=rattrapageService.getidSeanceRattEnseigant(dateRatt,idGroup);
        Double duree=saisieServiceSqlServer.getDuree(idSeanceAbsence);
        idSeancePreGroupe.add(0); idSeanceRattGroupe.add(0); idSeancePreEnseignant.add(0); idSeanceRattEnseigant.add(0);idSeanceGroupe.add(0);idSeanceEnseignant.add(0);
        return seanceRepository.getSeancesPossibles(idSeanceGroupe,idSeanceEnseignant,idSeancePreGroupe,idSeanceRattGroupe,idSeancePreEnseignant,idSeanceRattEnseigant,duree);
    }
    @GetMapping("/getPreSeancesPossibles/{dateRatt}/{idEnsiegnant}/{idAbsence}")
    public List<SeanceModel>getPreSeancesPossibles(@PathVariable Date dateRatt,@PathVariable int idEnsiegnant,@PathVariable int idAbsence )
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateRatt);
        int idJour=calendar.get(Calendar.DAY_OF_WEEK)-1;
        if (jourService.getJourById(idJour)==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Date de remplacement invalide");
        }
        AbsenceModel absence=absenceService.getAbsenceById(idAbsence);
        SaisieModelSqlServer saisie=saisieServiceSqlServer.getSeanceById(absence.getIdSeanceEnsiAbsence());
        List<Integer> idSeanceGroupe=saisieServiceSqlServer.getIdSeanceGroupe(idJour,saisie.getNiveau().getCOD_NIVEAU());
        List<Integer> idSeanceEnseignant=saisieServiceSqlServer.getIdSeanceEnseignant(idJour,idEnsiegnant);
        List<Integer> idSeancePreGroupe=preRattrapageService.getidSeancePreGroupe(dateRatt,saisie.getNiveau().getCOD_NIVEAU());
        List<Integer> idSeanceRattGroupe=rattrapageService.getidSeanceRattGroupe(dateRatt,saisie.getNiveau().getCOD_NIVEAU());
        List<Integer> idSeancePreEnseignant=preRattrapageService.getidSeancePreEnseignant(dateRatt,saisie.getNiveau().getCOD_NIVEAU());
        List<Integer> idSeanceRattEnseigant=rattrapageService.getidSeanceRattEnseigant(dateRatt,saisie.getNiveau().getCOD_NIVEAU());
        Double duree=saisie.getSeance().getDuree();
        idSeancePreGroupe.add(0); idSeanceRattGroupe.add(0); idSeancePreEnseignant.add(0); idSeanceRattEnseigant.add(0);idSeanceGroupe.add(0);idSeanceEnseignant.add(0);
        return seanceRepository.getSeancesPossibles(idSeanceGroupe,idSeanceEnseignant,idSeancePreGroupe,idSeanceRattGroupe,idSeancePreEnseignant,idSeanceRattEnseigant,duree);
    }
}
