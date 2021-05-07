package tn.isetsf.bpointage.model.MySql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="absence")
public class AbsenceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAbsence;
    private Date dateAbsence;
    private int idSeanceEnsiAbsence;
    private int idEnsiegnant;
    private int annee;
    private int semestre;
    private boolean verifier;
    @ManyToOne
    @JoinColumn(name = "idPointage")
    private PointageModel pointage;

    public AbsenceModel(Date dateAbsence, int idSeanceEnsiAbsence, int idEnsiegnant, int annee, int semestre, PointageModel pointage) {
        this.dateAbsence = dateAbsence;
        this.idSeanceEnsiAbsence = idSeanceEnsiAbsence;
        this.idEnsiegnant = idEnsiegnant;
        this.annee = annee;
        this.semestre = semestre;
        this.pointage = pointage;
    }
}
