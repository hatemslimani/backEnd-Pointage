package tn.isetsf.bpointage.service.MySql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.MySql.DepartementModel;
import tn.isetsf.bpointage.model.SqlServer.DepartementModelSqlServer;
import tn.isetsf.bpointage.repository.MySql.DepartementRepository;

import java.util.List;

@Service
public class DepartementService {
    @Autowired
    private DepartementRepository departementRepository;
    public void Store(DepartementModel d)
    {
        departementRepository.save(d);
    }
    public List<DepartementModel> getAll()
    {
        return departementRepository.findAll();
    }
    public DepartementModel StoreDepartement(DepartementModelSqlServer d)
    {
        DepartementModel departement =new DepartementModel();
        departement.setId(d.getCOD_dep());
        departement.setNom_dapartement(d.getNom_dep());
        return departementRepository.save(departement);
    }
    public DepartementModel getDepartement(int id)
    {
        return departementRepository.findById(id).orElse(null);
    }
    /*public DepartementModel toDepartement(DepartementModelSqlServer d)
    {
        d.get
        DepartementModel departement= new DepartementModel();
        departement.setNom_dapartement();
        return
    }*/
}
