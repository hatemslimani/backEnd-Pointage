package tn.isetsf.bpointage.model.MySql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SeanceAbsenceModel {
    private String Nom_matiere;
    private int num;
    private String nom_seance;
    private String nom_jour;

    public SeanceAbsenceModel(String nom_matiere, int num, String nom_seance, String nom_jour) {
        Nom_matiere = nom_matiere;
        this.num = num;
        this.nom_seance = nom_seance;
        this.nom_jour = nom_jour;
    }
}
