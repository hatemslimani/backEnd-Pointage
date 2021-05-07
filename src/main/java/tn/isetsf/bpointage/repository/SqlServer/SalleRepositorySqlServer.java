package tn.isetsf.bpointage.repository.SqlServer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.SqlServer.SalleModelSqlServer;
@Repository
public interface SalleRepositorySqlServer extends JpaRepository<SalleModelSqlServer,Integer> {
}
