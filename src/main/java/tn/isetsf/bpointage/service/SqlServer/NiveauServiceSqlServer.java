package tn.isetsf.bpointage.service.SqlServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.SqlServer.NiveauModelSqlServer;
import tn.isetsf.bpointage.repository.SqlServer.NiveauRepositorySqlServer;

import java.util.List;

@Service
public class NiveauServiceSqlServer {
    @Autowired
    private NiveauRepositorySqlServer niveauRepositorySqlServer;
    public List<NiveauModelSqlServer> getNiveauByEnsieg(int id,int year,int semestre)
    {
        return niveauRepositorySqlServer.findNiveauByEnsieg(id , year , semestre);
    }

    public String getByid(int idNiveau) {
        return niveauRepositorySqlServer.getbyid(idNiveau);
    }
}
