package tn.isetsf.bpointage.service.MySql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.MySql.PointageModel;
import tn.isetsf.bpointage.repository.MySql.PointageRepository;

import java.util.List;

@Service
public class PointageService {
    @Autowired
    private PointageRepository pointageRepository;
    public List<PointageModel> savePointages(List<PointageModel>p)
    {
        return pointageRepository.saveAll(p);
    }
    public List<PointageModel> getPointagesByIdControl(int idControl)
    {
        return pointageRepository.getPointagesByIdControl(idControl);
    }
}
