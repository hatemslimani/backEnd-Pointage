package tn.isetsf.bpointage.service.SqlServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.SqlServer.MatiereModelSqlServer;
import tn.isetsf.bpointage.repository.SqlServer.MatiereRepositorySqlServer;

@Service
public class MatierServiceSqlService {
    @Autowired
    private MatiereRepositorySqlServer matiereRepositorySqlServer;
    public MatiereModelSqlServer getById(int idMatier)
    {
        return matiereRepositorySqlServer.findById(idMatier).orElse(null);
    }
}
