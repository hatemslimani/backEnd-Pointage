package tn.isetsf.bpointage.repository.MySql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.MySql.BlockModel;

import java.util.List;

@Repository
public interface BlockRepository extends JpaRepository<BlockModel,Integer> {
    @Query("select b from BlockModel b where b.departement.id = ?1")
    List<BlockModel> findAllById_departement(@Param("id_departement") int id_departement);
    @Query("select b from BlockModel b order by b.nom_block asc ")
    List<BlockModel> findAllblock();
    @Query("select b from BlockModel b where b.etages is not empty order by b.nom_block asc")
    List<BlockModel> getBlocks();
}
