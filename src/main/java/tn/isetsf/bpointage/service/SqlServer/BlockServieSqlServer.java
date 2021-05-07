package tn.isetsf.bpointage.service.SqlServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.SqlServer.BlockModelSqlServer;
import tn.isetsf.bpointage.repository.SqlServer.BlockRepositorySqlServer;

import java.util.List;

@Service
public class BlockServieSqlServer {
    @Autowired
    private BlockRepositorySqlServer blockRepositorySqlServer;
    public List<BlockModelSqlServer> getAll()
    {
        return blockRepositorySqlServer.findAll();
    }
    public BlockModelSqlServer getBlock(int id)
    {
        return blockRepositorySqlServer.findById(id).orElse(null);
    }
}
