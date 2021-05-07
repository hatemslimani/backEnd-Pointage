package tn.isetsf.bpointage.model.SqlServer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="saisie")
public class SaisieModelSqlServer {
    @Id
    private int num;
    private int annee1;
    private int semestre1;
    private int par15;
    private int cours;
    private Double module;
    private Double coefC;
    private Double coefTP;
    private Double coefTD;
    @ManyToOne
    @JoinColumn(name = "salle1")
    private SalleModelSqlServer salle;
    @ManyToOne
    @JoinColumn(name = "matiere1")
    private MatiereModelSqlServer matiere;
    @ManyToOne
    @JoinColumn(name = "niveau1")
    private NiveauModelSqlServer niveau;
    @ManyToOne
    @JoinColumn(name = "ensi1")
    private EnsiegnantModelSqlServer ensiegnant;
    @ManyToOne
    @JoinColumn(name = "seance1")
    private SeanceModelSqlServer seance;
    @ManyToOne
    @JoinColumn(name = "jour1")
    private JourModelSqlServer jour;
}
