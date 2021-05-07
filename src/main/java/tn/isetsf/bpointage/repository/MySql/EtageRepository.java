package tn.isetsf.bpointage.repository.MySql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.MySql.EtageModel;

import java.util.List;

@Repository
public interface EtageRepository extends JpaRepository<EtageModel,Integer> {
    @Query("select e from EtageModel e where e.block.id=:idBlock order by e.NomEtage asc")
    List<EtageModel> getEtagesByBlock(int idBlock);
}
