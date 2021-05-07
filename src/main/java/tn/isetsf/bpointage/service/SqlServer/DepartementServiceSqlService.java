package tn.isetsf.bpointage.service.SqlServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.SqlServer.DepartementModelSqlServer;
import tn.isetsf.bpointage.repository.SqlServer.DepartementRepositorySqlServer;

import java.util.List;

@Service
public class DepartementServiceSqlService
{
    @Autowired
    private DepartementRepositorySqlServer departementRepository;
    public List<DepartementModelSqlServer> getAllDepartement()
    {
        return departementRepository.findAll();
    }
    public DepartementModelSqlServer getDepartement(int id)
    {
        return departementRepository.findById(id).orElse(null);
    }
}
