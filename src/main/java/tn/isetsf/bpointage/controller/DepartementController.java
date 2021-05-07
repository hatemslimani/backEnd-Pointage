package tn.isetsf.bpointage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.isetsf.bpointage.model.MySql.*;
import tn.isetsf.bpointage.model.entity.Etage;
import tn.isetsf.bpointage.service.MySql.BlockService;
import tn.isetsf.bpointage.service.MySql.DepartementService;
import tn.isetsf.bpointage.service.MySql.EtageService;
import tn.isetsf.bpointage.service.MySql.SalleService;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8100"})
@RestController
@RequestMapping("/departement")
public class DepartementController {
    @Autowired
    private DepartementService departementService;
    @Autowired
    private BlockService blockService;
    @Autowired
    private SalleService salleService;
    @Autowired
    private EtageService etageService;
    @GetMapping("/")
    public List<DepartementModel> getAllDepartement()
    {
        return departementService.getAll();
    }
    @GetMapping("/block")
    public List<BlockModel> getAllBlock()
    {
        return blockService.getAllBlock();
    }
    @GetMapping("/salle")
    public List<SalleModel> getAllSalleNotAffect()
    {
        return salleService.getAllSalleNotAffect();
    }
    @GetMapping("/{id}")
    public DepartementModel getDepartement(@PathVariable int id)
    {
        return departementService.getDepartement(id);
    }
    @PostMapping("/addEtage")
    public void addEtage(@RequestBody Etage e)
    {
        EtageModel etage =new EtageModel();
        etage.setNomEtage(e.getNomEtage());
        etage.setSalles(e.getSalles());
        etage.setBlock(blockService.getBlock(e.getIdBlock()));
        etageService.addEtage(etage);
    }
    @GetMapping("/etage")
    public List<Etage> getAllEtage()
    {
        List<EtageModel> etages=etageService.getAllEtage();
        List<Etage> eg = new ArrayList<>();
        for (EtageModel etage:etages) {
            Etage e=new Etage();
            e.setNomEtage(etage.getNomEtage());
            e.setIdEtage(etage.getId());
            e.setIdBlock(etage.getBlock().getId());
            e.setNomBlock(etage.getBlock().getNom_block());
            e.setSalles(salleService.getSallesByEtage(etage.getId()));
            eg.add(e);
        }
        return eg;
    }
    @GetMapping("/deleteEtage/{idEtage}")
    public void deleteEtage(@PathVariable int idEtage)
    {
        EtageModel etage=etageService.getEtageById(idEtage);
        if (etage==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Etage pas trouvé");
        }
        etageService.deleteEtage(etage);
        throw new ResponseStatusException(HttpStatus.OK,"supprimer avec success");
    }

}
