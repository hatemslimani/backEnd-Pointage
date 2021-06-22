package tn.isetsf.bpointage.model.MySql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="pointage")
public class PointageModel {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int idPointage;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_Salle")
    private SalleModel salle;
    private Boolean occupee;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_Control")
    private ControlModel control;
    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "idPointage",referencedColumnName = "idPointage")
    private List<AbsenceModel> absences=new ArrayList<>();
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    public PointageModel(SalleModel salle, Boolean occupee) {
        this.salle = salle;
        this.occupee = occupee;
    }
}
