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

    public List<RattrapageModel> getAllRattrapageByEnsie(List<Integer> idEnsiegnant,int year,int semestre) {
        return rattrapageRepository.getAllRattrapageByEnsie(idEnsiegnant,year,semestre);
    }

    public RattrapageModel getByDaterattBySalleBySeance(Date toDay, int idSalle, List<Integer> listSeance, int validee, boolean ensiegnee, int year, int semestre) {
        return rattrapageRepository.getByDaterattBySalleBySeance(toDay,idSalle,listSeance,validee,ensiegnee,year,semestre);
    }

    public RattrapageModel getById(int idRatt) {
        return rattrapageRepository.findById(idRatt).orElse(null);
    }

    public void deletePreRattrapage(RattrapageModel rattrapage) {
        rattrapageRepository.delete(rattrapage);
    }

    public void saveRattrapage(RattrapageModel rattrapageModel) {
        rattrapageRepository.save(rattrapageModel);
    }
    public void deleteEnseignantRatt(int id) {
        rattrapageRepository.deleteById(id);
    }
    public List<RattrapageModel> getAllByIdEnsie(int idEnsei) {
        return rattrapageRepository.getAllByIdEnsie(idEnsei);
    }

    public List<Integer> getidSeanceRattGroupe(Date dateRatt, int idGroup) {
        return rattrapageRepository.getidSeanceRattGroupe(dateRatt,idGroup);
    }

    public List<Integer> getidSeanceRattEnseigant(Date dateRatt, int idGroup) {
        return rattrapageRepository.getidSeanceRattEnseigant(dateRatt,idGroup);
    }

    public List<Integer> getSalleRatt(Date dateRatt, int idSeance) {
        return rattrapageRepository.getSalleRatt(dateRatt,idSeance);
    }
}
