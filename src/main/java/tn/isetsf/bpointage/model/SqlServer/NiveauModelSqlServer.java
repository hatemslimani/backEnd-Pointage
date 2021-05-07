package tn.isetsf.bpointage.model.SqlServer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="niveau")
public class NiveauModelSqlServer {
    @Id
    private int COD_NIVEAU;
    private String Nom_niveau;
    private int existant;
    private int niveau5;
    private int niveau;
    private int verrou;
    private int niv_21;
    private int verrou_sem2;
    private int nb_etudiant;
    @ManyToOne
    @JoinColumn(name = "filiere")
    private FiliereModelSqlServer filiere;

    public NiveauModelSqlServer(int COD_NIVEAU, String nom_niveau) {
        this.COD_NIVEAU = COD_NIVEAU;
        Nom_niveau = nom_niveau;
    }


}
