package tn.isetsf.bpointage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import tn.isetsf.bpointage.model.MySql.User;
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
    @Autowired
    private UserService userService;
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
        addAdmin();
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
            System.out.println("jours  "+jours.size());
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
    public void addAdmin()
    {
        List<User> users=userService.getUsers();
        if (users.isEmpty()) {
            User u = new User();
            u.setNom("admin");
            u.setPrenom("admin");
            u.setPassword("12345678");
            u.setRole("ADMIN");
            u.setFirstLogin(true);
            u.setUserName("admin@gmail.com");
            userService.CreateUser(u);
        }
    }
}
