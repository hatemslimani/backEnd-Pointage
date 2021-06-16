package tn.isetsf.bpointage.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.sql.Date;

@AllArgsConstructor
@Data
public class Remplacement {
    private int cod_rattrapage;
    private int idEnsiegnant;
    private Date dateAbsence;
    private int idNiveau;
    private int idSeanceAbsence;
    private Date dateRatt;
    private int idSalle;
    private int idSeance;
    private int idAbsence;
    private String nom_Ensi;
    private String Nom_niveau;
    private String nom_salle;
    private String nom_Seance;
    private String abNom_salle;
    private String abNom_Seance;
    private boolean ensiegnee;
    private int anne;
    private int semestre;
    private int status;
    private String matiere;
    /*public PreRattrapage(int cod_rattrapage, int idEnsiegnant,int idNiveau,String nom_Seance,String nom_salle, Date dateAbsence,  Date dateRatt, int anne, int semestre) {
        this.cod_rattrapage = cod_rattrapage;
        this.idEnsiegnant = idEnsiegnant;
        this.idNiveau = idNiveau;
        this.nom_salle = nom_salle;
        this.nom_Seance = nom_Seance;
        this.dateAbsence = dateAbsence;
        this.dateRatt = dateRatt;
        this.anne = anne;
        this.semestre = semestre;
    }*/

    public Remplacement(int cod_rattrapage, int idEnsiegnant, Date dateAbsence, int idNiveau, Date dateRatt, String nom_salle, String nom_Seance, int anne, int semestre) {
        this.cod_rattrapage = cod_rattrapage;
        this.idEnsiegnant = idEnsiegnant;
        this.dateAbsence = dateAbsence;
        this.idNiveau = idNiveau;
        this.dateRatt = dateRatt;
        this.nom_salle = nom_salle;
        this.nom_Seance = nom_Seance;
        this.anne = anne;
        this.semestre = semestre;
    }

    public Remplacement() {
    }
}
