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
    private String nomSeance;
    private String matier;
    private String nomNivean;
}
