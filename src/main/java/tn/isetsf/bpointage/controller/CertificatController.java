package tn.isetsf.bpointage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.isetsf.bpointage.model.MySql.AbsenceModel;
import tn.isetsf.bpointage.model.MySql.CertificatModel;
import tn.isetsf.bpointage.model.MySql.NotificationModel;
import tn.isetsf.bpointage.model.MySql.User;
import tn.isetsf.bpointage.model.SqlServer.EnsiegnantModelSqlServer;
import tn.isetsf.bpointage.model.entity.CertificatEntity;
import tn.isetsf.bpointage.service.MySql.AbsenceService;
import tn.isetsf.bpointage.service.MySql.CertificatService;
import tn.isetsf.bpointage.service.MySql.NotificationService;
import tn.isetsf.bpointage.service.MySql.UserService;
import org.apache.commons.io.FileUtils;
import tn.isetsf.bpointage.service.SqlServer.EnsiegnantServiceSqlServer;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8100"})
@RestController
@RequestMapping("/certificat")
public class CertificatController {
    @Autowired
    private CertificatService certificatService;
    @Autowired
    private UserService userService;
    @Autowired
    ServletContext context;
    @Autowired
    private EnsiegnantServiceSqlServer ensiegnantServiceSqlServer;
    @Autowired
    private AbsenceService absenceService;
    @Autowired
    private NotificationService notificationService;
    @PostMapping("/add")
    public void AddCertificat(@RequestParam("img") MultipartFile file,@RequestParam("certificat") String certi, @AuthenticationPrincipal UserDetails user1) throws IOException {
        CertificatEntity certificat = new ObjectMapper().readValue(certi, CertificatEntity.class);
        if (certificat.getStart() == null || certificat.getEnd() == null || certificat.getIdEnseignant() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "tous les informations sont obligatoires ");
        }
        boolean isExit = new File(context.getRealPath("/Images/")).exists();
        if (!isExit) {
            new File(context.getRealPath("/Images/")).mkdir();
        }
        String filename = file.getOriginalFilename();
        long millis = System.currentTimeMillis();
        String datetime = new Date().toGMTString();
        datetime = datetime.replace(" ", "");
        datetime = datetime.replace(":", "");
        String rndchars = RandomStringUtils.randomAlphanumeric(16);
        String newFileName = "img_" + rndchars + "_" + datetime + "_" + millis + "." + FilenameUtils.getExtension(filename);
        File serverFile = new File(context.getRealPath("/Images/" + File.separator + newFileName));
        try {
            FileUtils.writeByteArrayToFile(serverFile, file.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
        CertificatModel c = new CertificatModel();
        c.setStart(certificat.getStart());
        c.setEnd(certificat.getEnd());
        c.setImg(newFileName);
        c.setStatus(0);
        c.setIdEnseignant(certificat.getIdEnseignant());
        certificatService.add(c);
        User user = userService.getAdmin();
        if (user != null) {
                NotificationModel notificationModel = new NotificationModel();
                notificationModel.setTitle("Departement " + userService.getUserByEmail(user1.getUsername()).getDepartementt().getNom_dapartement());
                notificationModel.setMsg("L'enseignant " + ensiegnantServiceSqlServer.getEnsiegnantById(certificat.getIdEnseignant()).getNom_Ensi() + " a deposé son certificat");
                notificationModel.setIdReciver(user.getId());
                notificationModel.setVu(true);
                notificationService.save(notificationModel);
        }

    }
    @GetMapping("/getAllByDepartement")
    public List<CertificatEntity> getAllByDepartement(@AuthenticationPrincipal UserDetails user) throws IOException {
        User u=userService.getUserByEmail(user.getUsername());
        List<Integer> listIdEnsei=ensiegnantServiceSqlServer.getEnsiegnantBydepartement(u.getDepartementt().getId());
        List<CertificatModel> certificats=certificatService.getAllByDepartement(listIdEnsei);
        List<CertificatEntity> certificatEntities=new ArrayList<>();
        for (int i=0;i < certificats.size();i++)
        {
            CertificatEntity certificatEntity=new CertificatEntity(certificats.get(i).getIdCertificat(),certificats.get(i).getStart(),
                    certificats.get(i).getEnd(),certificats.get(i).getStatus(),certificats.get(i).getIdEnseignant());
            EnsiegnantModelSqlServer ensiegnantModelSqlServer=ensiegnantServiceSqlServer.getEnsiegnantById(certificats.get(i).getIdEnseignant());
            if (ensiegnantModelSqlServer != null)
            {
                certificatEntity.setNomEnseignant(ensiegnantModelSqlServer.getNom_Ensi());
            }
            String extension = FilenameUtils.getExtension(certificats.get(i).getImg());
            File file = new File(context.getRealPath("/Images/")+certificats.get(i).getImg());
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStream.read(bytes);
            String encodeBase64 = Base64.getEncoder().encodeToString(bytes);
            fileInputStream.close();
            certificatEntity.setImg("data:image/"+extension+";base64,"+encodeBase64);
            certificatEntities.add(certificatEntity);
        }
        return certificatEntities;
    }
    @GetMapping("/getAll")
    public List<CertificatEntity> getAllt() throws IOException {
        List<CertificatModel> certificats=certificatService.getAll();
        List<CertificatEntity> certificatEntities=new ArrayList<>();
        for (int i=0;i < certificats.size();i++)
        {
            CertificatEntity certificatEntity=new CertificatEntity(certificats.get(i).getIdCertificat(),certificats.get(i).getStart(),
                    certificats.get(i).getEnd(),certificats.get(i).getStatus(),certificats.get(i).getIdEnseignant());
            EnsiegnantModelSqlServer ensiegnantModelSqlServer=ensiegnantServiceSqlServer.getEnsiegnantById(certificats.get(i).getIdEnseignant());
            if (ensiegnantModelSqlServer != null)
            {
                certificatEntity.setNomEnseignant(ensiegnantModelSqlServer.getNom_Ensi());
            }
            String extension = FilenameUtils.getExtension(certificats.get(i).getImg());
            File file = new File(context.getRealPath("/Images/")+certificats.get(i).getImg());
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStream.read(bytes);
            String encodeBase64 = Base64.getEncoder().encodeToString(bytes);
            fileInputStream.close();
            certificatEntity.setImg("data:image/"+extension+";base64,"+encodeBase64);
            certificatEntities.add(certificatEntity);
        }
        return certificatEntities;
    }
    @GetMapping("/deleteById/{idCerti}")
    public void deleteById(@PathVariable int idCerti)
    {
        CertificatModel certificat=certificatService.getById(idCerti);
        if (certificat==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Justificatif n'existe pas");
        }
        if (certificat.getStatus()==1)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Suppression impossible"+"\n"+"Justificatif est déja validé" );
        }
        certificatService.deleteById(idCerti);
        File file = new File(context.getRealPath("/Images/")+certificat.getImg());
        file.delete();
        throw new ResponseStatusException(HttpStatus.OK,"suppression avec success" );
    }
    @GetMapping("/getById/{idCerti}")
    public CertificatEntity getById(@PathVariable int idCerti) throws IOException {
        CertificatModel certificatModel=certificatService.getById(idCerti);
        if (certificatModel.getImg()!= null)
        {
            String extension = FilenameUtils.getExtension(certificatModel.getImg());
            File file = new File(context.getRealPath("/Images/")+certificatModel.getImg());
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStream.read(bytes);
            String encodeBase64 = Base64.getEncoder().encodeToString(bytes);
            fileInputStream.close();
            certificatModel.setImg("data:image/"+extension+";base64,"+encodeBase64);
        }
        CertificatEntity c=new CertificatEntity(certificatModel.getIdCertificat(),
                certificatModel.getStart(),certificatModel.getEnd(),certificatModel.getImg(),certificatModel.getStatus(),
                ensiegnantServiceSqlServer.getEnsiegnantById(certificatModel.getIdEnseignant()).getNom_Ensi(),certificatModel.getCreatedAt());

        return c;
    }
    @PostMapping("/acceptCertificat")
    public void acceptCertificat(@RequestBody CertificatEntity certificatEntity)
    {
        CertificatModel certificatModel=certificatService.getById(certificatEntity.getIdCertificat());
        if (certificatModel==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Justificatif n'existe pas");
        }
        certificatModel.setStatus(certificatEntity.getStatus());
        certificatService.save(certificatModel);
        User user=userService.getResponsableByDepartement(ensiegnantServiceSqlServer.getEnsiegnantById(certificatModel.getIdEnseignant()).getDepartement().getCOD_dep());
        if (user != null) {
            NotificationModel notificationModel = new NotificationModel();
            notificationModel.setTitle("Administration ISET Sfax");
            notificationModel.setMsg("Acceptation justificatif, enseignant: "+ensiegnantServiceSqlServer.getByid(certificatModel.getIdEnseignant()));
            notificationModel.setIdReciver(user.getId());
            notificationModel.setVu(true);
            notificationService.save(notificationModel);
        }
        User enseignant=userService.getEnseignant(certificatModel.getIdEnseignant());
        if (enseignant != null) {
            NotificationModel notificationModel = new NotificationModel();
            notificationModel.setTitle("Administration ISET Sfax");
            notificationModel.setMsg("Votre justificatif a été accepté");
            notificationModel.setIdReciver(enseignant.getId());
            notificationModel.setVu(true);
            notificationService.save(notificationModel);
        }
        JustifierAbsences(certificatModel.getIdEnseignant(),certificatModel.getStart(),certificatModel.getEnd());
        throw new ResponseStatusException(HttpStatus.OK,"Justificatif accepté" );
    }
    public void JustifierAbsences(int idEnseignant,java.sql.Date dateDebut, java.sql.Date datefin)
    {
        List<AbsenceModel> absenceList=absenceService.getAbcensesNonVerifierByDate(idEnseignant,dateDebut,datefin);
        for (AbsenceModel absence:absenceList) {
            absence.setVerifier(true);
            absenceService.storeAbsence(absence);
        }
    }
    @PostMapping("/refuserCertificat")
    public void refuserCertificat(@RequestBody CertificatEntity certificatEntity)
    {
        CertificatModel certificatModel=certificatService.getById(certificatEntity.getIdCertificat());
        if (certificatModel==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Justificatif n'existe pas");
        }
        certificatModel.setStatus(certificatEntity.getStatus());
        certificatService.save(certificatModel);
        User user=userService.getResponsableByDepartement(ensiegnantServiceSqlServer.getEnsiegnantById(certificatModel.getIdEnseignant()).getDepartement().getCOD_dep());
        if (user != null ) {
            NotificationModel notificationModel = new NotificationModel();
            notificationModel.setTitle("Administration ISET Sfax");
            notificationModel.setMsg("Refus justificatif, enseignant: "+ensiegnantServiceSqlServer.getByid(certificatModel.getIdEnseignant()));
            notificationModel.setIdReciver(user.getId());
            notificationModel.setVu(true);
            notificationService.save(notificationModel);
        }
        User enseignant=userService.getEnseignant(certificatModel.getIdEnseignant());
        if (enseignant != null) {
            NotificationModel notificationModel = new NotificationModel();
            notificationModel.setTitle("Administration ISET Sfax");
            notificationModel.setMsg("Votre justificatif a été refusé pour certaines contraintes");
            notificationModel.setIdReciver(enseignant.getId());
            notificationModel.setVu(true);
            notificationService.save(notificationModel);
        }
        throw new ResponseStatusException(HttpStatus.OK,"Justificatif refusé" );
    }
}
