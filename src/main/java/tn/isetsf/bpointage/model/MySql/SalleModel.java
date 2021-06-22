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

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="salle")
@Entity
public class SalleModel {
    @Id
    private int Id;
    private String nom_salle;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_block")
    private BlockModel block;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_etage")
    private EtageModel etage;
    @OneToMany
    @JoinColumn(name = "id_Salle",referencedColumnName = "Id")
    @JsonIgnore
    List<PointageModel> pointages=new ArrayList<>();
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    public SalleModel(int id, String nom_salle) {
        Id = id;
        this.nom_salle = nom_salle;
    }
    public SalleModel(int id) {
        Id = id;
    }
}
