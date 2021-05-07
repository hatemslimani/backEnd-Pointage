package tn.isetsf.bpointage.model.MySql;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="prerattrapage")
public class PreRattrapageModel {
    @Id
    @GeneratedValue
    private int cod_rattrapage;
    private int idEnsiegnant;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateAbsence;
    private int idNiveau;
    private int idSeanceAbsence;
    private boolean validee;
    private boolean ensiegnee;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateRatt;
    @ManyToOne
    @JoinColumn(name = "idSalle")
    private SalleModel salle;
    @ManyToOne
    @JoinColumn(name = "idSeance")
    private SeanceModel Seance;
    private int annee;
    private int semestre;
}
