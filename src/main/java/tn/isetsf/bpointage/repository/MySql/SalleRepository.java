package tn.isetsf.bpointage.repository.MySql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.MySql.EtageModel;
import tn.isetsf.bpointage.model.MySql.SalleModel;

import java.util.List;

@Repository
public interface SalleRepository extends JpaRepository<SalleModel,Integer> {
    @Query("select s  from SalleModel s where s.block.id=?1")
    List<SalleModel> findAllByIdBlock(int id_block);
    @Query("select s from SalleModel  s where s.etage is null order by s.nom_salle asc ")
    List<SalleModel> findAllNotAffext();
    @Query("select s from SalleModel  s where s.etage.id=:idEtage order by s.nom_salle asc ")
    List<SalleModel> getSallesByEtage(int idEtage);
}
