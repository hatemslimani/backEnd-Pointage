package tn.isetsf.bpointage.repository.MySql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.MySql.AbsenceModel;

import java.util.List;

@Repository
public interface AbsenceRepository extends JpaRepository<AbsenceModel,Integer> {
    @Query("select a from AbsenceModel a where a.idEnsiegnant=:idEnseignant and a.verifier=false")
    List<AbsenceModel> getAbcensesNonVerifier(int idEnseignant);
}
