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
    private String token;
    private int idEnseignant;
    private int idDepartementt;
    private Boolean firstLogin;

    public UserEntity(String nom, String prenom, String userName, String role, String token,Boolean firstLogin) {
        this.nom = nom;
        this.prenom = prenom;
        this.userName = userName;
        this.role = role;
        this.token = token;
        this.firstLogin=firstLogin;
    }
}
