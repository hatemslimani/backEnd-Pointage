package tn.isetsf.bpointage.service.SqlServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.SqlServer.SeanceModelSqlServer;
import tn.isetsf.bpointage.repository.SqlServer.SeanceRepositorySqlServer;

import java.util.List;

@Service
public class SeanceServiceSqlServer {
    @Autowired
    private SeanceRepositorySqlServer seanceRepositorySqlService;
    public List<SeanceModelSqlServer> getAll()
    {
        return seanceRepositorySqlService.findAll();
    }
}
