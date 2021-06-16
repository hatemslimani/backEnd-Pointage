package tn.isetsf.bpointage.repository.SqlServer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.SqlServer.MatiereModelSqlServer;
@Repository
public interface MatiereRepositorySqlServer extends JpaRepository<MatiereModelSqlServer,Integer> {
}
