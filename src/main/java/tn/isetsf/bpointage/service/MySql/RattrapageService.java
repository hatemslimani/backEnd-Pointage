package tn.isetsf.bpointage.service.MySql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.MySql.RattrapageModel;
import tn.isetsf.bpointage.repository.MySql.RattrapageRepository;

import java.sql.Date;
import java.util.List;

@Service
public class RattrapageService {
    @Autowired
    private RattrapageRepository rattrapageRepository;
    public void storeRattrapage(RattrapageModel ratt)
    {
        rattrapageRepository.save(ratt);
    }

    public List<RattrapageModel> getAllRattrapageByEnsie(List<Integer> idEnsiegnant) {
        return rattrapageRepository.getAllRattrapageByEnsie(idEnsiegnant);
    }

    public RattrapageModel getByDaterattBySalleBySeance(Date toDay, int idSalle, List<Integer> listSeance, boolean validee, boolean ensiegnee, int year, int semestre) {
        return rattrapageRepository.getByDaterattBySalleBySeance(toDay,idSalle,listSeance,validee,ensiegnee,year,semestre);
    }
}
