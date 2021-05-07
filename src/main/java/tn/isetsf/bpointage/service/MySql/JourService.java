package tn.isetsf.bpointage.service.MySql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.MySql.JourModel;
import tn.isetsf.bpointage.model.SqlServer.JourModelSqlServer;
import tn.isetsf.bpointage.repository.MySql.JourRepository;

import java.util.List;

@Service
public class JourService {
    @Autowired
    private JourRepository jourRepository;
    public void storeJour(JourModelSqlServer j)
    {
        JourModel jour=new JourModel();
        jour.setCod_Jour(j.getCod_Jour());
        jour.setNom_Jour(j.getNom_Jour());
        jourRepository.save(jour);
    }
    public List<JourModel> getAll()
    {
        return jourRepository.findAll();
    }
    public JourModel getJourById(int idJour)
    {
        return jourRepository.findById(idJour).orElse(null);
    }
}
