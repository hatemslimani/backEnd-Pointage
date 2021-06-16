package tn.isetsf.bpointage.repository.MySql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.MySql.NotificationModel;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationModel,Integer> {
    @Query("select n from NotificationModel n where n.idReciver=:idUser and n.vu=true order by n.createdAt desc")
    List<NotificationModel> getNewNotification(int idUser);
    @Query("select n from NotificationModel n where n.idReciver=:idUser and n.vu=false order by n.createdAt desc")
    List<NotificationModel> getOldNotification(int idUser);
    @Query("select count(n) from NotificationModel n where n.idReciver=:idUser and n.vu=true")
    Integer getNbNewNotification(int idUser);
}
