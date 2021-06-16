package tn.isetsf.bpointage.controller;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import tn.isetsf.bpointage.model.MySql.*;
import tn.isetsf.bpointage.model.entity.Absence;
import tn.isetsf.bpointage.repository.MySql.*;
import tn.isetsf.bpointage.repository.SqlServer.EnsiegnantRepositorySqlServer;
import tn.isetsf.bpointage.repository.SqlServer.MatiereRepositorySqlServer;
import tn.isetsf.bpointage.repository.SqlServer.NiveauRepositorySqlServer;
import tn.isetsf.bpointage.repository.SqlServer.SaisieRepositorySqlServer;
import tn.isetsf.bpointage.service.MySql.DepartementService;
import tn.isetsf.bpointage.service.MySql.ReportService;
import tn.isetsf.bpointage.service.MySql.SeanceService;
import tn.isetsf.bpointage.service.MySql.UserService;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/report")
public class BilanController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private SeanceService seanceService;
    @Autowired
    private DepartementService departementService;
    @Autowired
    private UserService userService;
    @GetMapping("/byEnseignant/{dateDebut}/{dateFin}/{idEnsei}")
    public ResponseEntity<byte[]> exportListeAbsenceByEnseign(@PathVariable  Date dateDebut , @PathVariable Date dateFin, @PathVariable int idEnsei) throws IOException, JRException {
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/pdf; charset=UTF-8")
                .header("Content-Disposition", "inline; filename=\"" + "listeAbsence" + ".pdf\"")
                .body(reportService.exportListeAbsenceByEnsiegn(dateDebut,dateFin,idEnsei));
    }
    @GetMapping("/avisRatt/{id}")
    public ResponseEntity<byte[]> exportAvisRattrapage(@PathVariable int id) throws FileNotFoundException, JRException {
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/pdf; charset=UTF-8")
                .header("Content-Disposition", "inline; filename=\"" + "avisRattrapage" + ".pdf\"")
                .body(reportService.exportAvisRattrapage(id));
    }
    @GetMapping("/AvisPreRattrapage/{id}")
    public ResponseEntity<byte[]> exportAvisPreRattrapage(@PathVariable int id) throws FileNotFoundException, JRException {
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/pdf; charset=UTF-8")
                .header("Content-Disposition", "inline; filename=\"" + "avisPreRattrapage" + ".pdf\"")
                .body(reportService.exportAvisPreRattrapage(id));
    }
    @GetMapping("/list/{dateDebut}/{dateFin}")
    public ResponseEntity<byte[]> exportListeAbsence(@PathVariable  Date dateDebut ,@PathVariable Date dateFin) throws FileNotFoundException, JRException {
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/pdf; charset=UTF-8")
                .header("Content-Disposition", "inline; filename=\"" + "listeAbsence" + ".pdf\"")
                .body(reportService.exportListeAbsence(dateDebut,dateFin));
    }
    @GetMapping("/byDepartement/{dateDebut}/{dateFin}/{idDepartement}")
    public ResponseEntity<byte[]> exportListeAbsenceByDepartement(@PathVariable Date dateDebut ,@PathVariable Date dateFin, @PathVariable int idDepartement) throws FileNotFoundException, JRException {
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/pdf; charset=UTF-8")
                .header("Content-Disposition", "inline; filename=\"" + "listeAbsence" + ".pdf\"")
                .body(reportService.exportListeAbsenceByDepartement(dateDebut,dateFin,idDepartement));
    }
    @GetMapping("/byDepartementBySeance/{dateDebut}/{dateFin}/{idDepartement}/{idSeance}")
    public ResponseEntity<byte[]> exportListeAbsenceByDepartementBySeance(@PathVariable Date dateDebut ,@PathVariable Date dateFin, @PathVariable int idDepartement,@PathVariable int idSeance) throws FileNotFoundException, JRException {
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/pdf; charset=UTF-8")
                .header("Content-Disposition", "inline; filename=\"" + "listeAbsence" + ".pdf\"")
                .body(reportService.exportListeAbsenceByDepartementBySeance(dateDebut,dateFin,idDepartement,idSeance));
    }
    @GetMapping("/listBySeance/{dateDebut}/{dateFin}/{idSeance}")
    public ResponseEntity<byte[]> exportListeAbsenceBySeance(@PathVariable  Date dateDebut ,@PathVariable Date dateFin,@PathVariable int idSeance) throws FileNotFoundException, JRException {
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/pdf; charset=UTF-8")
                .header("Content-Disposition", "inline; filename=\"" + "listeAbsence" + ".pdf\"")
                .body(reportService.exportListeAbsenceBySeance(dateDebut,dateFin,idSeance));
    }
    @GetMapping("/byEnseignantBySeance/{dateDebut}/{dateFin}/{idEnsei}/{idSeance}")
    public ResponseEntity<byte[]> exportListeAbsenceByEnseignBySeance(@PathVariable  Date dateDebut , @PathVariable Date dateFin, @PathVariable int idEnsei,@PathVariable int idSeance) throws IOException, JRException {
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/pdf; charset=UTF-8")
                .header("Content-Disposition", "inline; filename=\"" + "listeAbsence" + ".pdf\"")
                .body(reportService.exportListeAbsenceByEnsiegnBySeance(dateDebut,dateFin,idEnsei,idSeance));
    }
    @GetMapping("/getAllSeance")
    public List<SeanceModel> getAllSeance()
    {
        return seanceService.getAll();
    }
    @GetMapping("/getAllDepartement")
    public List<DepartementModel> getAllDepartement()
    {
        return departementService.getAll();
    }

    @GetMapping("/respByDepartement/{dateDebut}/{dateFin}")
    public ResponseEntity<byte[]> exportRespListeAbsenceByDepartement(@PathVariable Date dateDebut ,@PathVariable Date dateFin,@AuthenticationPrincipal UserDetails user) throws FileNotFoundException, JRException {
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/pdf; charset=UTF-8")
                .header("Content-Disposition", "inline; filename=\"" + "listeAbsence" + ".pdf\"")
                .body(reportService.exportListeAbsenceByDepartement(dateDebut,dateFin,userService.getUserByEmail(user.getUsername()).getDepartementt().getId()));
    }
    @GetMapping("/RespByDepartementBySeance/{dateDebut}/{dateFin}/{idSeance}")
    public ResponseEntity<byte[]> exportRespListeAbsenceByDepartementBySeance(@PathVariable Date dateDebut ,@PathVariable Date dateFin,@PathVariable int idSeance,@AuthenticationPrincipal UserDetails user) throws FileNotFoundException, JRException {
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/pdf; charset=UTF-8")
                .header("Content-Disposition", "inline; filename=\"" + "listeAbsence" + ".pdf\"")
                .body(reportService.exportListeAbsenceByDepartementBySeance(dateDebut,dateFin,userService.getUserByEmail(user.getUsername()).getDepartementt().getId(),idSeance));
    }
}
