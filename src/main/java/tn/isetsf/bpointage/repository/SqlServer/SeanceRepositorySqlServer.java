package tn.isetsf.bpointage.repository.SqlServer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.SqlServer.SeanceModelSqlServer;
@Repository
public interface SeanceRepositorySqlServer extends JpaRepository<SeanceModelSqlServer,Integer> {
}
