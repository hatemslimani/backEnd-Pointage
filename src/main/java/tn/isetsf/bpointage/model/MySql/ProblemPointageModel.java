package tn.isetsf.bpointage.model.MySql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="ProblemPointage")
public class ProblemPointageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProblem;
    private Time timePointage;
    private Date datePointage;
    private String message;
    private int idPointage;
    private boolean vu;
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    public ProblemPointageModel(Time timePointage, Date datePointage, String message, int idPointage, boolean vu) {
        this.timePointage = timePointage;
        this.datePointage = datePointage;
        this.message = message;
        this.idPointage = idPointage;
        this.vu = vu;
    }
}
