package tn.isetsf.bpointage.repository.MySql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.MySql.JourModel;
@Repository
public interface JourRepository extends JpaRepository<JourModel,Integer> {
}
