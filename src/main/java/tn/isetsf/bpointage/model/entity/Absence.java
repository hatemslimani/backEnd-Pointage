package tn.isetsf.bpointage.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Absence {
    private int idAbsence;
    private Date dateAbsence;
    private String dateAbsencee;
    private String nomSeance;
    private String matier;
    private String nomNivean;
    private String nomEnseignant;
    public Absence(int idAbsence, Date dateAbsence, String nomSeance, String matier, String nomNivean) {
        this.idAbsence = idAbsence;
        this.dateAbsence = dateAbsence;
        this.nomSeance = nomSeance;
        this.matier = matier;
        this.nomNivean = nomNivean;
    }
}
