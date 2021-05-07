package tn.isetsf.bpointage.repository.MySql;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.MySql.User;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("select u from User u where u.userName=:username ")
    User findByEmail(String username);
}
