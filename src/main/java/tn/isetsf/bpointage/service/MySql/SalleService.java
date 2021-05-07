package tn.isetsf.bpointage.service.MySql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.MySql.*;
import tn.isetsf.bpointage.model.SqlServer.SalleModelSqlServer;
import tn.isetsf.bpointage.repository.MySql.SalleRepository;

import java.util.List;

@Service
public class SalleService {
    @Autowired
    private SalleRepository salleRepository;
    public List<SalleModel> getAll()
    {
        return salleRepository.findAll();
    }
    public void store(SalleModelSqlServer s)
    {
        SalleModel salle=new SalleModel();
        salle.setNom_salle(s.getNom_salle());
        salle.setId(s.getCOD_salle());
        salleRepository.save(salle);
    }
    public List<SalleModel> getAllSalleNotAffect()
    {
        return salleRepository.findAllNotAffext();
    }
    public List<SalleModel>getSallesByEtage(int idEtage) {
        return salleRepository.getSallesByEtage(idEtage);
    }
    public SalleModel getSalleById(int id)
    {
        return salleRepository.findById(id).orElse(null);
    }
}
