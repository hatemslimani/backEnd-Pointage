package tn.isetsf.bpointage.repository.MySql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.MySql.RattrapageModel;

import java.sql.Date;
import java.util.List;

@Repository
public interface RattrapageRepository extends JpaRepository<RattrapageModel,Integer> {
    @Query("select r from RattrapageModel r where r.idEnsiegnant in :idEnsiegnant and r.semestre=:semestre and r.annee=:year")
    List<RattrapageModel> getAllRattrapageByEnsie(List<Integer> idEnsiegnant,int year,int semestre);
    @Query("select r from RattrapageModel r where r.dateRatt=:toDay and r.salle.Id=:idSalle and r.Seance.id in :listSeance and r.status=:validee and r.ensiegnee =:ensiegnee and r.annee=:year and r.semestre=:semestre")
    RattrapageModel getByDaterattBySalleBySeance(Date toDay, int idSalle, List<Integer> listSeance, int validee, boolean ensiegnee, int year, int semestre);
    @Query("select count(r) from RattrapageModel r where r.idEnsiegnant in :listEnsei")
    int getNbByDepartement(List<Integer> listEnsei);
    @Query("select p from RattrapageModel p where p.idEnsiegnant=:idEnsei")
    List<RattrapageModel> getAllByIdEnsie(int idEnsei);
    @Query("select p.Seance.id from RattrapageModel  p where p.dateRatt=:dateRatt and p.idNiveau=:idGroup")
    List<Integer> getidSeanceRattGroupe(Date dateRatt, int idGroup);
    @Query("select p.Seance.id from RattrapageModel p where p.idNiveau=:idGroup and p.dateRatt=:dateRatt")
    List<Integer> getidSeanceRattEnseigant(Date dateRatt, int idGroup);
    @Query("select p.salle.Id from RattrapageModel p where p.dateRatt=:dateRatt and p.Seance.id=:idSeance")
    List<Integer> getSalleRatt(Date dateRatt, int idSeance);
    @Query("select p.absence.idAbsence from RattrapageModel p where p.idEnsiegnant=:idEnseignant and p.dateRatt>=:toDay")
    List<Integer> getRattrapageByEnseiByDate(int idEnseignant, Date toDay);
    @Query("select p.absence.idAbsence from RattrapageModel p where p.dateRatt>= :toDay and p.idEnsiegnant =:idEnseignant")
    List<Integer> getbyDate(Date toDay, int idEnseignant);
}
