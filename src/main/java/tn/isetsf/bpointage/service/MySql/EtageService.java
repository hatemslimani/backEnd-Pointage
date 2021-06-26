package tn.isetsf.bpointage.service.MySql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.MySql.EtageModel;
import tn.isetsf.bpointage.model.entity.Etage;
import tn.isetsf.bpointage.repository.MySql.BlockRepository;
import tn.isetsf.bpointage.repository.MySql.EtageRepository;

import java.util.List;

@Service
public class EtageService
{
    @Autowired
    private EtageRepository etageRepository;
    @Autowired
    private BlockRepository blockRepository;
    public List<EtageModel> getEtagesByBlock(int idBlock) {
        return etageRepository.getEtagesByBlock(idBlock);
    }

    public void addEtage(EtageModel e)
    {
        etageRepository.save(e);
    }
    public List<EtageModel>getAllEtage() {
        return etageRepository.findAll();
    }
    public EtageModel getEtageById(int idEtage)
    {
        return etageRepository.findById(idEtage).orElse(null);
    }
    public void deleteEtage(EtageModel e)
    {
        etageRepository.delete(e);
    }
    public void editEtage(EtageModel etage)
    {
        etageRepository.save(etage);
    }
}
