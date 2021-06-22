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
   @Query("select p from PreRattrapageModel p where p.idEnsiegnant in :idEnsiegnant and p.annee=:year and p.semestre=:semestre")
    List<PreRattrapageModel> findAllByEnsie(List<Integer> idEnsiegnant,int year , int semestre);
    @Query("select p from PreRattrapageModel p where p.idSeanceAbsence=:num and p.dateAbsence=:dateControl and p.idSeanceAbsence=:num and p.status=:validee and p.ensiegnee=:ensiegnee")
    PreRattrapageModel getBySAbsencebyDAbsence(int num, Date dateControl,int validee,boolean ensiegnee);
    @Query("select p from PreRattrapageModel p where p.dateRatt=:toDay and p.salle.Id=:idSalle and p.Seance.id in :listSeance and p.status=:validee and p.ensiegnee =:ensiegnee and p.annee=:year and p.semestre=:semestre")
    PreRattrapageModel getByDaterattBySalleBySeance(java.sql.Date toDay, int idSalle, List<Integer> listSeance, int validee, boolean ensiegnee,int year,int semestre);
    @Query("select count(p) from PreRattrapageModel p where p.idEnsiegnant in :listEnsei")
    int getNbByDepartement(List<Integer> listEnsei);
    @Query("select p from PreRattrapageModel p where p.idEnsiegnant=:idEnsei")
    List<PreRattrapageModel> getAllByIdEnsie(int idEnsei);
    @Query("select p.Seance.id from PreRattrapageModel p where p.dateRatt=:dateRatt and p.idNiveau=:idGroup")
    List<Integer> getidSeancePreGroupe(java.sql.Date dateRatt, int idGroup);
    @Query("select p.Seance.id from PreRattrapageModel p where p.dateRatt=:dateRatt and p.idNiveau=:idGroup")
    List<Integer> getidSeancePreEnseignant(java.sql.Date dateRatt, int idGroup);
    @Query("select p.salle.Id from PreRattrapageModel p where p.dateRatt=:dateRatt and p.Seance.id=:idSeance")
    List<Integer> getSallePre(java.sql.Date dateRatt, int idSeance);
}