package tn.isetsf.bpointage.service.SqlServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.SqlServer.EnsiegnantModelSqlServer;
import tn.isetsf.bpointage.repository.SqlServer.EnsiegnantRepositorySqlServer;

import java.util.List;

@Service
public class EnsiegnantServiceSqlServer {
    @Autowired
    private EnsiegnantRepositorySqlServer ensiegnantRepositorySqlServer;
    public List<EnsiegnantModelSqlServer> getEnsiegnantByName(String name)
    {
        return ensiegnantRepositorySqlServer.findAllbyName(name);
    }

    public List<Integer> getEnsiegnantBydepartement(int id) {
        return ensiegnantRepositorySqlServer.findAllBydepartement(id);
    }

    public String getByid(int idEnsiegnant) {
        return ensiegnantRepositorySqlServer.getNameEnsie(idEnsiegnant);
    }
}
