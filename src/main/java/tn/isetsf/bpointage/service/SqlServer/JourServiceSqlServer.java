package tn.isetsf.bpointage.service.SqlServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.SqlServer.JourModelSqlServer;
import tn.isetsf.bpointage.repository.SqlServer.JourRepositorySqlServer;

import java.util.List;

@Service
public class JourServiceSqlServer {
    @Autowired
    private JourRepositorySqlServer jourRepositorySqlServer;
    public List<JourModelSqlServer> getAllJour()
    {
        return jourRepositorySqlServer.findAll();
    }
}
