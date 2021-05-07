package tn.isetsf.bpointage.repository.MySql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.entity.Remplacement;
import tn.isetsf.bpointage.model.MySql.PreRattrapageModel;

import java.util.Date;
import java.util.List;

@Repository
public interface PreRattrapageRepossitory extends JpaRepository<PreRattrapageModel,Integer> {
   @Query("select p from PreRattrapageModel p where p.idEnsiegnant in :idEnsiegnant")
    List<PreRattrapageModel> findAllByEnsie(List<Integer> idEnsiegnant);
    @Query("select p from PreRattrapageModel p where p.idSeanceAbsence=:num and p.dateAbsence=:dateControl and p.idSeanceAbsence=:num and p.validee=:validee and p.ensiegnee=:ensiegnee")
    PreRattrapageModel getBySAbsencebyDAbsence(int num, Date dateControl,boolean validee,boolean ensiegnee);
    @Query("select p from PreRattrapageModel p where p.dateRatt=:toDay and p.salle.Id=:idSalle and p.Seance.id in :listSeance and p.validee=:validee and p.ensiegnee =:ensiegnee and p.annee=:year and p.semestre=:semestre")
    PreRattrapageModel getByDaterattBySalleBySeance(java.sql.Date toDay, int idSalle, List<Integer> listSeance, boolean validee, boolean ensiegnee,int year,int semestre);
    //List<PreRattrapageModel> findAllByDepartement(int id);
   /*@Query("select p from PreRattrapageModel p where p.idEnsiegnant in :idEnsiegnant")
   List<PreRattrapageModel> findAllByEnsie(List<Integer> idEnsiegnant);*/
}