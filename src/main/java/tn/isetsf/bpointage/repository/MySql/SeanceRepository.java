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
    @Query("select s from SeanceModel s where s.id not in :idSeanceGroupe and s.id not in :idSeanceEnseignant and s.id not in :idSeancePreGroupe and s.id not in :idSeanceRattGroupe and s.id not in :idSeancePreEnseignant and s.id not in :idSeanceRattEnseigant and s.duree=:duree")
    List<SeanceModel> getSeancesPossibles(List<Integer> idSeanceGroupe, List<Integer> idSeanceEnseignant, List<Integer> idSeancePreGroupe, List<Integer> idSeanceRattGroupe, List<Integer> idSeancePreEnseignant, List<Integer> idSeanceRattEnseigant, Double duree);
    @Query("select s.id from SeanceModel s where s.debutSeance>= (select ss.debutSeance from SeanceModel ss where ss.id=:idSeance) and s.finSeance<= (select sss.finSeance from SeanceModel sss where sss.id=:idSeance) ")
    List<Integer> getIdseanceensie(int idSeance);
}
