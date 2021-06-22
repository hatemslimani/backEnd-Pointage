package tn.isetsf.bpointage.model.MySql;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="rattrapage")
public class RattrapageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRattrapage;
    private int idEnsiegnant;
    private int idNiveau;
    private int idSeanceAbsence;
    private int status;
    private boolean ensiegnee;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateRatt;
    @ManyToOne
    @JoinColumn(name = "idSallee")
    private SalleModel salle;
    @ManyToOne
    @JoinColumn(name = "idSeancee")
    private SeanceModel Seance;
    private int annee;
    private int semestre;
    @ManyToOne
    @JoinColumn(name = "idAbsence")
    private AbsenceModel absence;
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;
}
