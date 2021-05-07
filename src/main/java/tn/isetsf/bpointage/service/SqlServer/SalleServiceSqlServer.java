package tn.isetsf.bpointage.service.SqlServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.SqlServer.SalleModelSqlServer;
import tn.isetsf.bpointage.repository.SqlServer.SalleRepositorySqlServer;

import java.util.List;

@Service
public class SalleServiceSqlServer {
    @Autowired
    private SalleRepositorySqlServer salleRepositorySqlServer;
    public List<SalleModelSqlServer> getAll()
    {
        return salleRepositorySqlServer.findAll();
    }
}
