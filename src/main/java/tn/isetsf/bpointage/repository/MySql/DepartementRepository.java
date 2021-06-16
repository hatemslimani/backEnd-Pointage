package tn.isetsf.bpointage.repository.MySql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.MySql.DepartementModel;

import java.util.List;

@Repository
public interface DepartementRepository extends JpaRepository<DepartementModel,Integer> {
    @Query("select d from DepartementModel d where d.nom_dapartement <> 'AUCUN' order by d.nom_dapartement asc ")
    List<DepartementModel> getAll();
    @Query("select d from DepartementModel  d where d.id not in :list and d.nom_dapartement <> 'AUCUN'")
    List<DepartementModel> getdepartementNotResponsable(List<Integer> list);
}
