package tn.isetsf.bpointage.repository.SqlServer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.SqlServer.JourModelSqlServer;
@Repository
public interface JourRepositorySqlServer extends JpaRepository<JourModelSqlServer,Integer> {
}
