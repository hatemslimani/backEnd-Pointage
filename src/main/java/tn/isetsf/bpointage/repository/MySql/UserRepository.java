package tn.isetsf.bpointage.repository.MySql;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.MySql.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("select u from User u where u.userName=:username ")
    User findByEmail(String username);
    @Query("select u.idEnseignant from User u where u.role='ENSEIGNANT'")
    List<Integer> enseignantSignIn();
    @Query("select u.departementt.id from User u where u.role='RESPONSABLE'")
    List<Integer> getdepartementResponsable();
    @Query("select e from User e where e.idEnseignant=:idEnsei")
    User getEnseigant(int idEnsei);
    @Query("select u from User u where u.departementt.id=:iddep")
    User getResponsableByDepartement(int iddep);
    @Query("select  u from User u where u.role='ADMIN'")
    User getAdmin();
}
