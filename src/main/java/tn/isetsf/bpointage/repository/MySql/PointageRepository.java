package tn.isetsf.bpointage.repository.MySql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.MySql.PointageModel;

import java.util.List;

@Repository
public interface PointageRepository extends JpaRepository<PointageModel,Integer> {
    @Query("select p from PointageModel p where p.control.idControl=:idControl")
    List<PointageModel> getPointagesByIdControl(int idControl);
}
