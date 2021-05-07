package tn.isetsf.bpointage.repository.MySql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.MySql.DepartementModel;

@Repository
public interface DepartementRepository extends JpaRepository<DepartementModel,Integer> {

}
