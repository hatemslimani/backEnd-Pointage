package tn.isetsf.bpointage.service.MySql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.MySql.DepartementModel;
import tn.isetsf.bpointage.model.MySql.User;
import tn.isetsf.bpointage.model.SqlServer.DepartementModelSqlServer;
import tn.isetsf.bpointage.model.entity.AdminCardDash;
import tn.isetsf.bpointage.repository.MySql.*;
import tn.isetsf.bpointage.repository.SqlServer.EnsiegnantRepositorySqlServer;

import java.util.List;

@Service
public class DepartementService {
    @Autowired
    private DepartementRepository departementRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private SalleRepository salleRepository;
    @Autowired
    private PreRattrapageRepossitory preRattrapageRepossitory;
    @Autowired
    private RattrapageRepository rattrapageRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EnsiegnantRepositorySqlServer ensiegnantRepositorySqlServer;
    public void Store(DepartementModel d)
    {
        departementRepository.save(d);
    }
    public List<DepartementModel> getAll()
    {
        return departementRepository.getAll();
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

    public List<DepartementModel> getdepartementNotResponsable() {
        List<Integer> list=userRepository.getdepartementResponsable();
        if (list.isEmpty()) return getAll();
        return departementRepository.getdepartementNotResponsable(list);
    }

    public AdminCardDash getadminCardDash() {
        AdminCardDash ad=new AdminCardDash();
        ad.setNbUser(userRepository.findAll().size());
        ad.setNbBlock(blockRepository.findAll().size());
        ad.setNbSalle(salleRepository.findAll().size());
        return ad;
    }

    public AdminCardDash getRespCardDash(int idDep) {
        AdminCardDash ad=new AdminCardDash();
        List<Integer> listEnsei=ensiegnantRepositorySqlServer.findAllBydepartement(idDep);
        ad.setNbRattrapage(rattrapageRepository.getNbByDepartement(listEnsei));
        ad.setNbPreRattrapage(preRattrapageRepossitory.getNbByDepartement(listEnsei));
        return ad;
    }
    /*public DepartementModel toDepartement(DepartementModelSqlServer d)
    {
        d.get
        DepartementModel departement= new DepartementModel();
        departement.setNom_dapartement();
        return
    }*/
}
