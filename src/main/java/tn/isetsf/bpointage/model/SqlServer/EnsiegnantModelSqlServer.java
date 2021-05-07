package tn.isetsf.bpointage.model.SqlServer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="enseignant")
public class EnsiegnantModelSqlServer {
    @Id
    private int COD_Enseig;
    private String nom_Ensi;
    @ManyToOne
    @JoinColumn(name = "departement1")
    private DepartementModelSqlServer departement;

    public EnsiegnantModelSqlServer(int COD_Enseig, String nom_Ensi) {
        this.COD_Enseig = COD_Enseig;
        this.nom_Ensi = nom_Ensi;
    }
}
