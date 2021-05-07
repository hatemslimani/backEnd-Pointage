package tn.isetsf.bpointage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tn.isetsf.bpointage.model.SqlServer.*;
import tn.isetsf.bpointage.repository.SqlServer.SaisieRepositorySqlServer;
import tn.isetsf.bpointage.service.MySql.*;
import tn.isetsf.bpointage.service.SqlServer.*;

import java.util.List;

@SpringBootApplication
public class BpointageApplication implements CommandLineRunner {
    @Autowired
    private DepartementServiceSqlService departementServiceSqlService;
    @Autowired
    private DepartementService departementService;
    @Autowired
    private JourServiceSqlServer jourServiceSqlServer;
    @Autowired
    private JourService jourService;
    @Autowired
    private SeanceServiceSqlServer seanceServiceSqlServer;
    @Autowired
    private SeanceService seanceService;
    @Autowired
    private BlockServieSqlServer blockServieSqlServer;
    @Autowired
    private BlockService blockService;
    @Autowired
    private SalleServiceSqlServer salleServiceSqlServer;
    @Autowired
    private SalleService salleService;
    @Autowired
    private SaisieRepositorySqlServer saisieRepositorySqlServer;
    public static void main(String[] args) {
        SpringApplication.run(BpointageApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        getBlocks();
        getdepartements();
        getsalles();
        getJour();
        getSeance();
    }
    public void getBlocks()
    {
        if (blockService.getAll().isEmpty())
        {
            List<BlockModelSqlServer> blocks=blockServieSqlServer.getAll();
            for (BlockModelSqlServer block:blocks)
            {
                blockService.store(block);
            }
        }
    }
    public void getdepartements()
    {
        if (departementService.getAll().isEmpty())
        {
            List<DepartementModelSqlServer> departements=departementServiceSqlService.getAllDepartement();
            for (DepartementModelSqlServer departement:departements)
            {
                departementService.StoreDepartement(departement);
            }
        }
    }
    public void getsalles()
    {
        if (salleService.getAll().isEmpty())
        {
            List<SalleModelSqlServer> salles=salleServiceSqlServer.getAll();
            for (SalleModelSqlServer salle:salles)
            {
                salleService.store(salle);
            }
        }
    }
    public void getJour()
    {
        if (jourService.getAll().isEmpty()) {
            List<JourModelSqlServer> jours = jourServiceSqlServer.getAllJour();
            for (JourModelSqlServer jour : jours)
            {
                jourService.storeJour(jour);
            }
        }
    }
    public void getSeance()
    {
        if(seanceService.getAll().isEmpty())
        {
            List<SeanceModelSqlServer> seances=seanceServiceSqlServer.getAll();
            for (SeanceModelSqlServer seance : seances)
            {
                seanceService.storeSeance(seance);
            }
        }
    }
}
