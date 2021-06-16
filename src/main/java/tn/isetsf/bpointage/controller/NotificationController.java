package tn.isetsf.bpointage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import tn.isetsf.bpointage.model.MySql.NotificationModel;
import tn.isetsf.bpointage.service.MySql.NotificationService;
import tn.isetsf.bpointage.service.MySql.UserService;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;
    @GetMapping("getNewNotification")
    public List<NotificationModel> getNewNotification(@AuthenticationPrincipal UserDetails user)
    {
        return notificationService.getNewNotification(userService.getUserByEmail(user.getUsername()).getId());
    }
    @GetMapping("getNbNewNotification")
    public Integer getNbNewNotification(@AuthenticationPrincipal UserDetails user)
    {
        return notificationService.getNbNewNotification(userService.getUserByEmail(user.getUsername()).getId());
    }
    @PostMapping("/MarkVue")
    private void markerVu(@RequestBody List<NotificationModel> notificationList) {
        for (NotificationModel notification:notificationList) {
            notification.setVu(false);
            notificationService.save(notification);
        }
    }

}
