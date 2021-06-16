package tn.isetsf.bpointage.service.MySql;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import tn.isetsf.bpointage.model.MySql.AbsenceModel;
import tn.isetsf.bpointage.model.MySql.DepartementModel;
import tn.isetsf.bpointage.model.MySql.PreRattrapageModel;
import tn.isetsf.bpointage.model.MySql.RattrapageModel;
import tn.isetsf.bpointage.model.SqlServer.SaisieModelSqlServer;
import tn.isetsf.bpointage.model.entity.Absence;
import tn.isetsf.bpointage.repository.MySql.*;
import tn.isetsf.bpointage.repository.SqlServer.EnsiegnantRepositorySqlServer;
import tn.isetsf.bpointage.repository.SqlServer.NiveauRepositorySqlServer;
import tn.isetsf.bpointage.repository.SqlServer.SaisieRepositorySqlServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
    @Autowired
    private RattrapageRepository rattrapageRepository;
    @Autowired
    private EnsiegnantRepositorySqlServer ensiegnantRepositorySqlServer;
    @Autowired
    private NiveauRepositorySqlServer niveauRepositorySqlServer;
    @Autowired
    private PreRattrapageRepossitory preRattrapageRepossitory;
    @Autowired
    private DepartementRepository departementRepository;
    @Autowired
    private SaisieRepositorySqlServer saisieRepositorySqlServer;
    @Autowired
    private AbsenceRepository absenceRepository;
    public byte[] exportAvisRattrapage(int id) throws FileNotFoundException, JRException {
        RattrapageModel rattrapage = rattrapageRepository.findById(id).orElse(null);
        File file = ResourceUtils.getFile("classpath:RattrapageAvis.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        SaisieModelSqlServer saisie=saisieRepositorySqlServer.findById(rattrapage.getIdSeanceAbsence()).orElse(null);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("dateRatt", rattrapage.getDateRatt()+"");
        parameters.put("matiere", saisie.getMatiere().getNom_matiere());
        parameters.put("ensiegnant", ensiegnantRepositorySqlServer.findById(rattrapage.getIdEnsiegnant()).orElse(null).getNom_Ensi());
        parameters.put("groupe",niveauRepositorySqlServer.findById(rattrapage.getIdNiveau()).orElse(null).getNom_niveau());
        parameters.put("ratSeance", rattrapage.getSeance().getNom_Seance());
        parameters.put("ratSalle", rattrapage.getSalle().getNom_salle());
        parameters.put("abDate",rattrapage.getAbsence().getDateAbsence()+"");
        parameters.put("abSeance", saisie.getSeance().getNom_Seance());
        parameters.put("abSalle", saisie.getSalle().getNom_salle());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,new JREmptyDataSource());

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    public byte[] exportAvisPreRattrapage(int id) throws FileNotFoundException, JRException {
        PreRattrapageModel rattrapage = preRattrapageRepossitory.findById(id).orElse(null);
        if (rattrapage !=null) {
            File file = ResourceUtils.getFile("classpath:PreRattrapageAvis.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            SaisieModelSqlServer saisie=saisieRepositorySqlServer.findById(rattrapage.getIdSeanceAbsence()).orElse(null);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("datePre", rattrapage.getDateRatt() + "");
            parameters.put("matiere", saisie.getMatiere().getNom_matiere());
            parameters.put("ensiegnant", ensiegnantRepositorySqlServer.findById(rattrapage.getIdEnsiegnant()).orElse(null).getNom_Ensi());
            parameters.put("groupe", niveauRepositorySqlServer.findById(rattrapage.getIdNiveau()).orElse(null).getNom_niveau());
            parameters.put("AbSeance", saisie.getSeance().getNom_Seance());
            parameters.put("abSalle", saisie.getSalle().getNom_salle());
            parameters.put("PreSeance", rattrapage.getSeance().getNom_Seance());
            parameters.put("preSalle", rattrapage.getSalle().getNom_salle());
            parameters.put("abDate", rattrapage.getDateAbsence() + "");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }
        return null;
    }
    public byte[] exportListeAbsence(Date dateDebut , Date dateFin) throws FileNotFoundException, JRException {
        List<AbsenceModel> absenceModelList=absenceRepository.getAbsenceByDate(dateDebut,dateFin);
        List<Absence> listAbsences=new ArrayList<>();
        for (AbsenceModel a:absenceModelList) {
            Absence absence=new Absence();
            absence.setDateAbsencee(a.getDateAbsence()+"");
            absence.setNomSeance(saisieRepositorySqlServer.findById(a.getIdSeanceEnsiAbsence()).orElse(null).getSeance().getNom_Seance());
            absence.setNomNivean(saisieRepositorySqlServer.findById(a.getIdSeanceEnsiAbsence()).orElse(null).getNiveau().getNom_niveau());
            absence.setNomEnseignant(saisieRepositorySqlServer.findById(a.getIdSeanceEnsiAbsence()).orElse(null).getEnsiegnant().getNom_Ensi());
            absence.setMatier(saisieRepositorySqlServer.findById(a.getIdSeanceEnsiAbsence()).orElse(null).getMatiere().getNom_matiere());
            listAbsences.add(absence);
        }
        File file = ResourceUtils.getFile("classpath:ListeAbsence.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listAbsences);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("dateDebut", dateDebut+"");
        parameters.put("dateFin", dateFin+"");
        parameters.put("nbAbsence", listAbsences.size());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    public byte[] exportListeAbsenceByDepartement(Date dateDebut , Date dateFin,int idDepartement) throws FileNotFoundException, JRException {
        DepartementModel departementModel=departementRepository.findById(idDepartement).orElse(null);
        List<Integer> listeEnseignant=ensiegnantRepositorySqlServer.findAllBydepartement(idDepartement);
        List<AbsenceModel> absenceModelList=absenceRepository.getAbsenceByDateByDepartement(dateDebut,dateFin,listeEnseignant);
        List<Absence> listAbsences=new ArrayList<>();
        for (AbsenceModel a:absenceModelList) {
            Absence absence=new Absence();
            absence.setDateAbsencee(a.getDateAbsence()+"");
            absence.setNomSeance(saisieRepositorySqlServer.findById(a.getIdSeanceEnsiAbsence()).orElse(null).getSeance().getNom_Seance());
            absence.setNomNivean(saisieRepositorySqlServer.findById(a.getIdSeanceEnsiAbsence()).orElse(null).getNiveau().getNom_niveau());
            absence.setNomEnseignant(saisieRepositorySqlServer.findById(a.getIdSeanceEnsiAbsence()).orElse(null).getEnsiegnant().getNom_Ensi());
            listAbsences.add(absence);
        }
        File file = ResourceUtils.getFile("classpath:ListeAbsenceByDepartement.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listAbsences);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("dateDebut", dateDebut+"");
        parameters.put("dateFin", dateFin+"");
        parameters.put("nbAbsence", listAbsences.size());
        parameters.put("departement", departementModel.getNom_dapartement());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    public byte[] exportListeAbsenceByEnsiegn(Date dateDebut , Date dateFin,int idEnsei) throws FileNotFoundException, JRException {
        List<AbsenceModel> absenceModelList=absenceRepository.getAbsenceByDateByEnseign(dateDebut,dateFin,idEnsei);
        List<Absence> listAbsences=new ArrayList<>();
        for (AbsenceModel a:absenceModelList) {
            Absence absence=new Absence();
            absence.setDateAbsencee(a.getDateAbsence()+"");
            absence.setNomSeance(saisieRepositorySqlServer.findById(a.getIdSeanceEnsiAbsence()).orElse(null).getSeance().getNom_Seance());
            absence.setNomNivean(saisieRepositorySqlServer.findById(a.getIdSeanceEnsiAbsence()).orElse(null).getNiveau().getNom_niveau());
            listAbsences.add(absence);
        }
        File file = ResourceUtils.getFile("classpath:ListeAbsenceByEnsi.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listAbsences);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("dateDebut", dateDebut+"");
        parameters.put("dateFin", dateFin+"");
        parameters.put("nbAbsence", listAbsences.size());
        parameters.put("nom", ensiegnantRepositorySqlServer.findById(idEnsei).orElse(null).getNom_Ensi());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public byte[] exportListeAbsenceByDepartementBySeance(Date dateDebut, Date dateFin, int idDepartement, int idSeance) throws FileNotFoundException, JRException {
        DepartementModel departementModel=departementRepository.findById(idDepartement).orElse(null);
        List<Integer> listeEnseignant=ensiegnantRepositorySqlServer.findAllBydepartement(idDepartement);
        List<Integer> listeSaisie=saisieRepositorySqlServer.getIdSaisiesByDeapretemntBySeance(idSeance,listeEnseignant);
        List<AbsenceModel> absenceModelList=absenceRepository.getAbsenceByDateByDepartementBySeance(dateDebut,dateFin,listeEnseignant,listeSaisie);
        List<Absence> listAbsences=new ArrayList<>();
        for (AbsenceModel a:absenceModelList) {
            Absence absence=new Absence();
            absence.setDateAbsencee(a.getDateAbsence()+"");
            absence.setNomSeance(saisieRepositorySqlServer.findById(a.getIdSeanceEnsiAbsence()).orElse(null).getSeance().getNom_Seance());
            absence.setNomNivean(saisieRepositorySqlServer.findById(a.getIdSeanceEnsiAbsence()).orElse(null).getNiveau().getNom_niveau());
            absence.setNomEnseignant(saisieRepositorySqlServer.findById(a.getIdSeanceEnsiAbsence()).orElse(null).getEnsiegnant().getNom_Ensi());
            listAbsences.add(absence);
        }
        File file = ResourceUtils.getFile("classpath:ListeAbsenceByDepartement.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listAbsences);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("dateDebut", dateDebut+"");
        parameters.put("dateFin", dateFin+"");
        parameters.put("nbAbsence", listAbsences.size());
        parameters.put("departement", departementModel.getNom_dapartement());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public byte[] exportListeAbsenceBySeance(Date dateDebut , Date dateFin,int idSeance) throws FileNotFoundException, JRException {
        List<Integer> listeSaisie=saisieRepositorySqlServer.getIdSaisiesBySeance(idSeance);
        List<AbsenceModel> absenceModelList=absenceRepository.getAbsenceByDateBySeance(dateDebut,dateFin,listeSaisie);
        List<Absence> listAbsences=new ArrayList<>();
        for (AbsenceModel a:absenceModelList) {
            Absence absence=new Absence();
            absence.setDateAbsencee(a.getDateAbsence()+"");
            absence.setNomSeance(saisieRepositorySqlServer.findById(a.getIdSeanceEnsiAbsence()).orElse(null).getSeance().getNom_Seance());
            absence.setNomNivean(saisieRepositorySqlServer.findById(a.getIdSeanceEnsiAbsence()).orElse(null).getNiveau().getNom_niveau());
            absence.setNomEnseignant(saisieRepositorySqlServer.findById(a.getIdSeanceEnsiAbsence()).orElse(null).getEnsiegnant().getNom_Ensi());
            absence.setMatier(saisieRepositorySqlServer.findById(a.getIdSeanceEnsiAbsence()).orElse(null).getMatiere().getNom_matiere());
            listAbsences.add(absence);
        }
        File file = ResourceUtils.getFile("classpath:ListeAbsence.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listAbsences);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("dateDebut", dateDebut+"");
        parameters.put("dateFin", dateFin+"");
        parameters.put("nbAbsence", listAbsences.size());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public byte[] exportListeAbsenceByEnsiegnBySeance(Date dateDebut, Date dateFin, int idEnsei, int idSeance) throws JRException, FileNotFoundException {
        List<Integer> listeSaisie=saisieRepositorySqlServer.getIdSaisiesBySeance(idSeance);
        List<AbsenceModel> absenceModelList=absenceRepository.getAbsenceByDateByEnseignBySeance(dateDebut,dateFin,idEnsei,listeSaisie);
        List<Absence> listAbsences=new ArrayList<>();
        for (AbsenceModel a:absenceModelList) {
            Absence absence=new Absence();
            absence.setDateAbsencee(a.getDateAbsence()+"");
            absence.setNomSeance(saisieRepositorySqlServer.findById(a.getIdSeanceEnsiAbsence()).orElse(null).getSeance().getNom_Seance());
            absence.setNomNivean(saisieRepositorySqlServer.findById(a.getIdSeanceEnsiAbsence()).orElse(null).getNiveau().getNom_niveau());
            listAbsences.add(absence);
        }
        File file = ResourceUtils.getFile("classpath:ListeAbsenceByEnsi.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listAbsences);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("dateDebut", dateDebut+"");
        parameters.put("dateFin", dateFin+"");
        parameters.put("nbAbsence", listAbsences.size());
        parameters.put("nom", ensiegnantRepositorySqlServer.findById(idEnsei).orElse(null).getNom_Ensi());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
