package tn.isetsf.bpointage.model.MySql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USER_TBL")
public class User {
    @Id
    @GeneratedValue
    private int id;
    private String nom;
    private String prenom;
    private String userName;
    //@JsonIgnore
    private String password;
    private String role;
    private int idEnseignant;
    private Boolean firstLogin;
    @ManyToOne
    @JoinColumn(name = "idDepartementt")
    private DepartementModel departementt;
}
