package tn.isetsf.bpointage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.isetsf.bpointage.model.MySql.SalleModel;
import tn.isetsf.bpointage.repository.MySql.SalleRepository;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/salle")
public class SalleController {
    @Autowired
    private SalleRepository salleRepository;
    @GetMapping("/{id_Block}")
    public List<SalleModel> getAllByDepartement(@PathVariable int id_Block)
    {
        return salleRepository.findAllByIdBlock(id_Block);
    }
}
