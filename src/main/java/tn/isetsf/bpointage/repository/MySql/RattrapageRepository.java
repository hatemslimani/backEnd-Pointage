package tn.isetsf.bpointage.repository.MySql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.MySql.RattrapageModel;

import java.sql.Date;
import java.util.List;

@Repository
public interface RattrapageRepository extends JpaRepository<RattrapageModel,Integer> {
    @Query("select r from RattrapageModel r where r.idEnsiegnant in :idEnsiegnant")
    List<RattrapageModel> getAllRattrapageByEnsie(List<Integer> idEnsiegnant);
    @Query("select r from RattrapageModel r where r.dateRatt=:toDay and r.salle.Id=:idSalle and r.Seance.id in :listSeance and r.validee=:validee and r.ensiegnee =:ensiegnee and r.annee=:year and r.semestre=:semestre")
    RattrapageModel getByDaterattBySalleBySeance(Date toDay, int idSalle, List<Integer> listSeance, boolean validee, boolean ensiegnee, int year, int semestre);
}
