package tn.isetsf.bpointage.repository.MySql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.MySql.ProblemPointageModel;
@Repository
public interface ProblemPointageRepository extends JpaRepository<ProblemPointageModel,Integer> {
    @Query("select count(p) from ProblemPointageModel p where p.vu=false order by p.datePointage desc , p.timePointage desc ")
    Integer getNbNewProblemPointage();
}
