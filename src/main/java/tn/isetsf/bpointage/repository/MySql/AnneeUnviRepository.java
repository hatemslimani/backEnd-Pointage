package tn.isetsf.bpointage.repository.MySql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.MySql.AnneeUnviModel;

import java.sql.Date;

@Repository
public interface AnneeUnviRepository extends JpaRepository<AnneeUnviModel,Integer> {
    @Query("select a from AnneeUnviModel a where a.start <= :toDay and a.end >= :toDay ")
    AnneeUnviModel getAll(Date toDay);
}
