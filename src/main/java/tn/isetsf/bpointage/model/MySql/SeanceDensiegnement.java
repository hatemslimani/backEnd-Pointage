package tn.isetsf.bpointage.model.MySql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SeanceDensiegnement {
    private int COD_matiere;
    private String abv;
    private String Nom_matiere;
    private String nom_jour;
    private String Nom_niveau;
    private int ensi1;

    public SeanceDensiegnement(int COD_matiere, String abv, String nom_matiere, String nom_jour, String nom_niveau, int ensi1) {
        this.COD_matiere = COD_matiere;
        this.abv = abv;
        Nom_matiere = nom_matiere;
        this.nom_jour = nom_jour;
        Nom_niveau = nom_niveau;
        this.ensi1 = ensi1;
    }

    public SeanceDensiegnement(int COD_matiere, String abv, String nom_matiere, String nom_jour, String nom_niveau) {
        this.COD_matiere = COD_matiere;
        this.abv = abv;
        Nom_matiere = nom_matiere;
        this.nom_jour = nom_jour;
        Nom_niveau = nom_niveau;
    }
}
