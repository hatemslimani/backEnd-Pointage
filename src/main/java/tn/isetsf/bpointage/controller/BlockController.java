package tn.isetsf.bpointage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.isetsf.bpointage.model.MySql.BlockModel;
import tn.isetsf.bpointage.service.MySql.BlockService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/block")
public class BlockController {
    @Autowired
    private BlockService blockService;
    @GetMapping("/{id_departement}")
    public List<BlockModel> getAllBlockByDepartement(@PathVariable int id_departement)
    {
        return blockService.getAllBlocksbyDepartement(id_departement);
    }
}
