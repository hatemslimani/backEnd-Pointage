package tn.isetsf.bpointage.service.MySql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.MySql.NotificationModel;
import tn.isetsf.bpointage.repository.MySql.NotificationRepository;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public void save(NotificationModel notificationModel) {
        notificationRepository.save(notificationModel);
    }

    public List<NotificationModel> getNewNotification(int idUser) {
        return notificationRepository.getNewNotification(idUser);
    }

    public List<NotificationModel> getOldNotification(int idUser) {
        return notificationRepository.getOldNotification(idUser);
    }

    public Integer getNbNewNotification(int idUser) {
        return notificationRepository.getNbNewNotification(idUser);
    }
}
