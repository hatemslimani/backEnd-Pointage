package tn.isetsf.bpointage.model.SqlServer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="filiere")
public class FiliereModelSqlServer {
    @Id
    private int code_f;
    private String nom_f;
    @ManyToOne
    @JoinColumn(name = "dep2")
    private DepartementModelSqlServer departement;
}
