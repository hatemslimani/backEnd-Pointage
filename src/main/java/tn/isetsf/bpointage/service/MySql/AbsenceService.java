package tn.isetsf.bpointage.service.MySql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.MySql.AbsenceModel;
import tn.isetsf.bpointage.model.MySql.AnneeUnviModel;
import tn.isetsf.bpointage.repository.MySql.AbsenceRepository;
import tn.isetsf.bpointage.service.SqlServer.EnsiegnantServiceSqlServer;

import java.sql.Date;
import java.util.List;

@Service
public class AbsenceService {
    @Autowired
    private AbsenceRepository absenceRepository;
    @Autowired
    private EnsiegnantServiceSqlServer ensiegnantServiceSqlServer;
    @Autowired
    private RattrapageService rattrapageService;
    public List<Date> getDatesAbsences(AnneeUnviModel anneeUnviModel) {
         return absenceRepository.getDatesAbsences(anneeUnviModel.getStart(),anneeUnviModel.getEnd());
    }

    public AbsenceModel storeAbsence(AbsenceModel absence)
    {
        return absenceRepository.save(absence);
    }
    public AbsenceModel getAbsenceById(int id)
    {
        return absenceRepository.findById(id).orElse(null);
    }

    public List<AbsenceModel> getAbcensesNonVerifier(int idEnseignant) {
        Date toDay= new java.sql.Date(new java.util.Date().getTime());
        List<Integer> listIdPre=rattrapageService.getRattrapageByEnseiByDate(idEnseignant,toDay);
        listIdPre.add(0);
        return absenceRepository.getAbcensesNonVerifier(idEnseignant,listIdPre);
    }
    public int getNbAbsenceBydep(int idDep)
    {
        List<Integer> listIdEnsei=ensiegnantServiceSqlServer.getEnsiegnantBydepartement(idDep);
        return absenceRepository.getNbAbsenceBydep(listIdEnsei);
    }

    public int getAllByDateByDep(Date date,int idDep ) {
        List<Integer> listIdEnsei=ensiegnantServiceSqlServer.getEnsiegnantBydepartement(idDep);
        return absenceRepository.getAllByDateByDep(date,listIdEnsei);
    }
    public List<AbsenceModel> getAbsenceByIdEnseignant(int year, int semestre, int idEnsei) {
        return absenceRepository.getAbcenseByIdEnseignant(year,semestre,idEnsei);

    }
    public List<AbsenceModel> getAbsenceByIdEnseignantt(int year, int semestre, int idEnsei,List<Integer> listPre) {
        return absenceRepository.getAbcenseByIdEnseignantt(year,semestre,idEnsei,listPre);

    }

    public List<AbsenceModel> getAbcensesNonVerifierByDate(int idEnseignant,Date dateDebut, Date datefin) {
        return absenceRepository.getAbcensesNonVerifierByDate(idEnseignant,dateDebut,datefin);
    }
}
