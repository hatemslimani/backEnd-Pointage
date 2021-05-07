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

    public List<PreRattrapageModel> getAllByEnsie(List<Integer> idEnsiegnant) {
        return preRattrapageRepossitory.findAllByEnsie(idEnsiegnant);
    }

    public PreRattrapageModel getBySAbsencebyDAbsence(int num, Date dateControl,boolean validee,boolean ensiegnee) {
        return preRattrapageRepossitory.getBySAbsencebyDAbsence(num,dateControl,validee,ensiegnee);
    }

    public PreRattrapageModel getByDaterattBySalleBySeance(java.sql.Date toDay, int idSalle, List<Integer> listSeance, boolean validee, boolean ensiegnee,int year,int semestre) {
        return preRattrapageRepossitory.getByDaterattBySalleBySeance(toDay,idSalle,listSeance,validee,ensiegnee,year,semestre);
    }
    /*public List<PreRattrapageModel>getAll(int id)
    {
        return preRattrapageRepossitory.findAllByDepartement(id);
    }*/
}
