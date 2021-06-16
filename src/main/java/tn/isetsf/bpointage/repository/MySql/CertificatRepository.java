package tn.isetsf.bpointage.repository.MySql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.MySql.CertificatModel;

import java.util.List;

@Repository
public interface CertificatRepository extends JpaRepository<CertificatModel,Integer> {
    @Query("select c from CertificatModel c where c.idEnseignant in :listIdEnsei")
    List<CertificatModel> getAllByDepartement(List<Integer> listIdEnsei);
}
