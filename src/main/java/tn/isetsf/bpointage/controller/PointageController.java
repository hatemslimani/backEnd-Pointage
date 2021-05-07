package tn.isetsf.bpointage.controller;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.isetsf.bpointage.model.MySql.*;
import tn.isetsf.bpointage.model.SqlServer.SaisieModelSqlServer;
import tn.isetsf.bpointage.model.entity.Block;
import tn.isetsf.bpointage.model.entity.Pointage;
import tn.isetsf.bpointage.model.entity.Etage;
import tn.isetsf.bpointage.service.MySql.*;
import tn.isetsf.bpointage.service.SqlServer.SaisieServiceSqlServer;
import org.joda.time.Interval;
import java.sql.Time;
import java.text.ParseException;
import java.util.*;

@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8100"})
@RestController
@RequestMapping("/pointage")
public class PointageController {
    @Autowired
    private BlockService blockService;
    @Autowired
    private EtageService etageService;
    @Autowired
    private SalleService salleService;
    @Autowired
    private JourService jourService;
    @Autowired
    private SeanceService seanceService;
    @Autowired
    private ControlService controlService;
    @Autowired
    private PointageService pointageService;
    @Autowired
    private SaisieServiceSqlServer saisieServiceSqlServer;
    @Autowired
    private PreRattrapageService preRattrapageService;
    @Autowired
    private AbsenceService absenceService;
    @Autowired
    private CalendarService calendarService;
    @Autowired
    private RattrapageService rattrapageService;
    @Autowired
    private ProblemPointageService problemPointageService;
    Time time;
    java.sql.Date toDay;
    Calendar calendar;
    int idJour;
    JourModel jour;
    AnneeUnviModel anneeUniv;
    List<EventModel> events;
    SeanceModel seance;

    public void init() {
        this.toDay= new java.sql.Date(new java.util.Date().getTime());
        this.calendar = Calendar.getInstance();
        calendar.setTime(toDay);
        this.idJour=calendar.get(Calendar.DAY_OF_WEEK)-1;
        this.time = Time.valueOf(java.time.LocalTime.now());
        this.jour =jourService.getJourById(idJour);
        this.anneeUniv=calendarService.getAll(toDay);
        this.events=calendarService.getEventsByDate(toDay);
        this.seance=seanceService.getSeanceByTime(time);
    }

    public void Valider()
    {
        init();
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
        if (this.jour==null || this.anneeUniv==null || !this.events.isEmpty() || this.seance==null || semestre == 0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Hors service");
        }
    }
    @GetMapping("/seance")
    public SeanceModel getSeance()
    {
        init();
        return this.seance;
    }
    @GetMapping("/blocks")
    public List<Block> getBlocks()
    {
        Valider();
        List<BlockModel> blockModelList =blockService.getBlocks();
        List<Block> blocks=new ArrayList<>();
        for (BlockModel block:blockModelList) {
            ControlModel control=controlService.getByBlockByDateBySeanceByJour(block.getId(),this.toDay,this.seance.getId(),this.jour.getCod_Jour());
            Block b=new Block(block.getId(),block.getNom_block(),false);
            if(control!= null)
            {
                b.setPointee(true);
            }
            blocks.add(b);
        }
        return blocks;
    }
    @GetMapping("/etages/{idBlock}")
    public List<Etage> getEtagesByBlock(@PathVariable int idBlock)
    {
        Valider();
        List<EtageModel>e=etageService.getEtagesByBlock(idBlock);
        List<Etage>etages=new ArrayList<>();
        for (EtageModel etage: e) {
            Etage et=new Etage(etage.getId(),etage.getBlock().getNom_block(),etage.getBlock().getId(),etage.getNomEtage(),salleService.getSallesByEtage(etage.getId()));
            etages.add(et);
        }
        return etages;
    }
    @PostMapping("/{idBlock}")
    public ControlModel Pointage(@PathVariable int idBlock , @RequestBody List<Pointage> p) {
        Valider();
        //test if pointage fait
        List<PointageModel> pointages=new ArrayList<>();
        for (Pointage pt:p) {
            PointageModel pointageModel=new PointageModel(salleService.getSalleById(pt.getId_Salle()),pt.getOccupee());
            pointages.add(pointageModel);
        }
        pointageService.savePointages(pointages);
        ControlModel control=new ControlModel();
        control.setDateControl(this.toDay);
        control.setBlock(blockService.getBlock(idBlock));
        control.setJour(this.jour);
        control.setSeance(this.seance);
        control.setTempsControl(this.time);
        control.setPointages(pointages);
        ControlModel c=controlService.addControl(control);
        verfierAbsence(c);
        return c;
    }

    private void verfierAbsence(ControlModel c) {
        List<PointageModel> pointages=pointageService.getPointagesByIdControl(c.getIdControl());
        int year=calendar.get(Calendar.YEAR);
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
        for (PointageModel pointage: pointages) {
            SaisieModelSqlServer seanceEnsiegnenment=saisieServiceSqlServer.verfierSeanceEnsiegnenment(pointage.getSalle().getId(),c.getJour().getCod_Jour(),c.getSeance().getId(),year,semestre);
            if(seanceEnsiegnenment==null)
            {
                if (pointage.getOccupee()==true)
                {
                    List<Integer> listSeance=seanceService.getSeancesByTime(time);
                    PreRattrapageModel preRattrapage=preRattrapageService.getByDaterattBySalleBySeance(toDay,pointage.getSalle().getId(),listSeance,true,false,year,semestre);
                    if(preRattrapage != null)
                    {
                        preRattrapage.setEnsiegnee(true);
                        preRattrapageService.addPre(preRattrapage);
                    }
                    else
                    {
                        RattrapageModel rattrapage=rattrapageService.getByDaterattBySalleBySeance(toDay,pointage.getSalle().getId(),listSeance,true,false,year,semestre);
                        if(rattrapage != null)
                        {
                            rattrapage.setEnsiegnee(true);
                            rattrapageService.storeRattrapage(rattrapage);
                        }
                        else
                        {
                            String message="Vous ete marquee que la salle "+pointage.getSalle().getNom_salle()+
                                    " est en cours d'ensiegnement mais aucun seance d'ensiegnement, preRattrapage, rattrapage dans ce date";
                            ProblemPointageModel problem=new ProblemPointageModel(this.time,this.toDay,
                                    message,pointage.getIdPointage(),false);
                            problemPointageService.storeProblemPointage(problem);

                        }
                    }
                }
            }
            else
            {
                if (pointage.getOccupee()==false)
                {
                    PreRattrapageModel preRattrapage=preRattrapageService.getBySAbsencebyDAbsence(seanceEnsiegnenment.getNum(),c.getDateControl(),true,true);
                    if(preRattrapage == null)
                    {
                        AbsenceModel absence=new AbsenceModel(c.getDateControl(),seanceEnsiegnenment.getNum(),
                                seanceEnsiegnenment.getEnsiegnant().getCOD_Enseig(),seanceEnsiegnenment.getAnnee1(),
                                seanceEnsiegnenment.getSemestre1(),pointage);
                        absenceService.storeAbsence(absence);
                        //send email to teacher
                    }
                }
            }
        }
    }
    @GetMapping("/problem")
    public List<ProblemPointageModel> getAllProblemPointage()
    {
        return problemPointageService.getAllProblemPointage();
    }
    @GetMapping("/test")
    public void test() throws ParseException {
        ControlModel c=controlService.findd(537);
        verfierAbsence(c);
    }
    @GetMapping("/testt")
    public void testt() {
        init();
        List<Integer> listSeance=seanceService.getSeancesByTime(time);
        System.out.println(listSeance);
    }
    @GetMapping("/problem/new")
    public Integer getNbNewProblemPointage()
    {
        return problemPointageService.getNbNewProblemPointage();
    }
}
