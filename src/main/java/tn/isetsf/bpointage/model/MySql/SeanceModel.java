package tn.isetsf.bpointage.model.MySql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Time;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "seance")
public class SeanceModel {
    @Id
    @GeneratedValue
    private int id;
    private String nom_Seance;
    private Time debutSeance;
    private Time finSeance;
    private Double duree;

    public SeanceModel(int id) {
        this.id = id;
    }
}
