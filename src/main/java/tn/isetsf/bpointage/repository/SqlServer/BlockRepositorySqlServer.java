package tn.isetsf.bpointage.repository.SqlServer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.SqlServer.BlockModelSqlServer;
@Repository
public interface BlockRepositorySqlServer extends JpaRepository<BlockModelSqlServer,Integer> {
}
