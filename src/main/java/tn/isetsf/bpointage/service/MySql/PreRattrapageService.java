package tn.isetsf.bpointage.service.MySql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.entity.Remplacement;
import tn.isetsf.bpointage.model.MySql.PreRattrapageModel;
import tn.isetsf.bpointage.repository.MySql.PreRattrapageRepossitory;

import java.util.Date;
import java.util.List;

@Service
public class PreRattrapageService {
    @Autowired
    private PreRattrapageRepossitory preRattrapageRepossitory;
    public void addPre(PreRattrapageModel p)
    {
        preRattrapageRepossitory.save(p);
    }

    public List<PreRattrapageModel> getAllByEnsie(List<Integer> idEnsiegnant,int year,int semestre) {
        return preRattrapageRepossitory.findAllByEnsie(idEnsiegnant,year,semestre);
    }

    public PreRattrapageModel getBySAbsencebyDAbsence(int num, Date dateControl,int validee,boolean ensiegnee) {
        return preRattrapageRepossitory.getBySAbsencebyDAbsence(num,dateControl,validee,ensiegnee);
    }

    public PreRattrapageModel getByDaterattBySalleBySeance(java.sql.Date toDay, int idSalle, List<Integer> listSeance, int status, boolean ensiegnee,int year,int semestre) {
        return preRattrapageRepossitory.getByDaterattBySalleBySeance(toDay,idSalle,listSeance,status,ensiegnee,year,semestre);
    }
    /*public List<PreRattrapageModel>getAll(int id)
    {
        return preRattrapageRepossitory.findAllByDepartement(id);
    }*/
    public PreRattrapageModel getById(int idPre)
    {
        return preRattrapageRepossitory.findById(idPre).orElse(null);
    }
    public void deletePreRattrapage(PreRattrapageModel p)
    {
        preRattrapageRepossitory.delete(p);
    }

    public void savePreRattrapage(PreRattrapageModel preRattrapageModel) {
        preRattrapageRepossitory.save(preRattrapageModel);
    }
    public void deleteEnseignantPreRatt(int id)
    {
        preRattrapageRepossitory.deleteById(id);
    }
    public List<PreRattrapageModel> getAllByIdEnsie(int idEnsei) {
        return preRattrapageRepossitory.getAllByIdEnsie(idEnsei);
    }

    public List<Integer> getidSeancePreGroupe(java.sql.Date dateRatt, int idGroup) {
        return preRattrapageRepossitory.getidSeancePreGroupe(dateRatt,idGroup);
    }

    public List<Integer> getidSeancePreEnseignant(java.sql.Date dateRatt, int idGroup) {
        return preRattrapageRepossitory.getidSeancePreEnseignant(dateRatt,idGroup);
    }

    public List<Integer> getSallePre(java.sql.Date dateRatt, int idSeance) {
        return preRattrapageRepossitory.getSallePre(dateRatt,idSeance);
    }
}
