package tn.isetsf.bpointage.repository.MySql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.MySql.ControlModel;
import tn.isetsf.bpointage.model.MySql.SeanceModel;

import java.sql.Date;

@Repository
public interface ControlRepository extends JpaRepository<ControlModel,Integer> {
    @Query("select c from ControlModel c where c.block.id=:idBlock and c.jour.cod_Jour=:cod_jour and c.dateControl=:toDay and c.seance.id=:idSeance")
    ControlModel getByBlockByDateBySeanceByJour(int idBlock, Date toDay, int idSeance, int cod_jour);
    @Query("select c from ControlModel c where c.block.id=:idBlock and c.dateControl=:toDay and c.seance.id=:seance")
    ControlModel getByVerification(int idBlock, Date toDay, int seance);
}
