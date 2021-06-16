package tn.isetsf.bpointage.repository.MySql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.MySql.SeanceModel;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface SeanceRepository extends JpaRepository<SeanceModel,Integer> {
    @Query("select s from SeanceModel s where s.debutSeance <= :t and s.finSeance >= :t and s.duree=1.5")
    SeanceModel findSeanceByTime(Time t);
    @Query("select s.id from SeanceModel s where s.debutSeance<= :time and s.finSeance >= :time")
    List<Integer> getSeancesByTime(Time time);
    @Query("select s from SeanceModel s order by s.debutSeance asc,s.duree asc ")
    List<SeanceModel> getAll();
}
