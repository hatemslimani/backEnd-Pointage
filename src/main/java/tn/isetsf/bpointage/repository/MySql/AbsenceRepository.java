package tn.isetsf.bpointage.repository.MySql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.MySql.AbsenceModel;

import java.sql.Date;
import java.util.List;

@Repository
public interface AbsenceRepository extends JpaRepository<AbsenceModel,Integer> {
    @Query("select a from AbsenceModel a where a.idEnsiegnant=:idEnseignant and a.verifier=false")
    List<AbsenceModel> getAbcensesNonVerifier(int idEnseignant);
    @Query("select a from AbsenceModel a where a.dateAbsence>= :dateDebut and a.dateAbsence<= :dateFin and a.verifier=false order by a.dateAbsence asc,a.pointage.control.tempsControl asc ")
    List<AbsenceModel> getAbsenceByDate(Date dateDebut, Date dateFin);
    @Query("select a from AbsenceModel a where a.dateAbsence>= :dateDebut and a.dateAbsence<= :dateFin and a.verifier=false and a.idEnsiegnant in :listeEnseignant order by a.dateAbsence asc,a.pointage.control.tempsControl asc ")
    List<AbsenceModel> getAbsenceByDateByDepartement(Date dateDebut, Date dateFin, List<Integer> listeEnseignant);
    @Query("select a from AbsenceModel a where a.dateAbsence>= :dateDebut and a.dateAbsence<= :dateFin and a.verifier=false and a.idEnsiegnant =:idEnsei order by a.dateAbsence asc,a.pointage.control.tempsControl asc ")
    List<AbsenceModel> getAbsenceByDateByEnseign(Date dateDebut, Date dateFin, int idEnsei);
    @Query("select count(a) from AbsenceModel a where a.idEnsiegnant in :listIdEnsei")
    int getNbAbsenceBydep(List<Integer> listIdEnsei);
    @Query("select distinct a.dateAbsence from AbsenceModel a where a.dateAbsence >= :startt and a.dateAbsence <= :endd order by a.dateAbsence asc ")
    List<Date> getDatesAbsences(Date startt, Date endd);
    @Query("select count(a) from AbsenceModel a where a.dateAbsence=:date and a.idEnsiegnant in :listIdEnsei")
    int getAllByDateByDep(Date date,List<Integer> listIdEnsei);
    @Query("select ab from AbsenceModel ab where ab.annee =:year and ab.semestre =:semestre and ab.idEnsiegnant =:idEnsei  and ab.verifier=false order by ab.dateAbsence desc")
    List<AbsenceModel> getAbcenseByIdEnseignant(int year, int semestre, int idEnsei);
    @Query("select ab from AbsenceModel ab where ab.idEnsiegnant=:idEnseignant and ab.dateAbsence>= :dateDebut and ab.dateAbsence<= :datefin and ab.verifier=false")
    List<AbsenceModel> getAbcensesNonVerifierByDate(int idEnseignant,Date dateDebut, Date datefin);
    @Query("select ab from AbsenceModel ab where ab.idSeanceEnsiAbsence in :listeSaisie and ab.dateAbsence>= :dateDebut and ab.dateAbsence<= :dateFin and ab.verifier=false and ab.idEnsiegnant in :listeEnseignant order by ab.dateAbsence asc,ab.pointage.control.tempsControl asc ")
    List<AbsenceModel> getAbsenceByDateByDepartementBySeance(Date dateDebut, Date dateFin, List<Integer> listeEnseignant, List<Integer> listeSaisie);
    @Query("select ab from AbsenceModel ab where ab.idSeanceEnsiAbsence in :listeSaisie and ab.dateAbsence>= :dateDebut and ab.dateAbsence<= :dateFin and ab.verifier=false order by ab.dateAbsence asc,ab.pointage.control.tempsControl asc ")
    List<AbsenceModel> getAbsenceByDateBySeance(Date dateDebut, Date dateFin, List<Integer> listeSaisie);
    @Query("select a from AbsenceModel a where a.dateAbsence>= :dateDebut and a.dateAbsence<= :dateFin and a.verifier=false and a.idEnsiegnant =:idEnsei and a.idSeanceEnsiAbsence in :listeSaisie order by a.dateAbsence asc,a.pointage.control.tempsControl asc ")
    List<AbsenceModel> getAbsenceByDateByEnseignBySeance(Date dateDebut, Date dateFin, int idEnsei, List<Integer> listeSaisie);
}
