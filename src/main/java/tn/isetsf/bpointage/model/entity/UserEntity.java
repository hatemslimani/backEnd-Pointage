package tn.isetsf.bpointage.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    private int id;
    private String nom;
    private String prenom;
    private String userName;
    private String password;
    private String role;
    private String departement;
    private String  newPassword;
}
