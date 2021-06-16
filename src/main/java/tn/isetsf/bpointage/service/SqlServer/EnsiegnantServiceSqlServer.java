package tn.isetsf.bpointage.service.SqlServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.MySql.User;
import tn.isetsf.bpointage.model.SqlServer.EnsiegnantModelSqlServer;
import tn.isetsf.bpointage.repository.MySql.UserRepository;
import tn.isetsf.bpointage.repository.SqlServer.EnsiegnantRepositorySqlServer;

import java.util.List;

@Service
public class EnsiegnantServiceSqlServer {
    @Autowired
    private EnsiegnantRepositorySqlServer ensiegnantRepositorySqlServer;
    @Autowired
    private UserRepository userRepository;
    public List<EnsiegnantModelSqlServer> getEnsiegnantByName(String name,int idDep)
    {
        return ensiegnantRepositorySqlServer.findAllbyNameByDep(name,idDep);
    }

    public List<Integer> getEnsiegnantBydepartement(int id) {
        return ensiegnantRepositorySqlServer.findAllBydepartement(id);
    }

    public String getByid(int idEnsiegnant) {
        return ensiegnantRepositorySqlServer.getNameEnsie(idEnsiegnant);
    }

    public List<EnsiegnantModelSqlServer> getEnseignantsNotSignIn(String name) {
        List<Integer> enseignantSignIn=userRepository.enseignantSignIn();
        if (!enseignantSignIn.isEmpty()){
            return ensiegnantRepositorySqlServer.getEnseignantsNotSignIn(name,enseignantSignIn);
        }
        else {
            return ensiegnantRepositorySqlServer.getEnseignantsNotSignIn(name);
        }
    }
    public EnsiegnantModelSqlServer getEnsiegnantById(int id)
    {
        return ensiegnantRepositorySqlServer.findById(id).orElse(null);
    }

    public List<EnsiegnantModelSqlServer> getAllEnsiegnantByName(String name) {
        return ensiegnantRepositorySqlServer.findAllbyName(name);
    }
}
