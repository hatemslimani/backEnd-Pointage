package tn.isetsf.bpointage.model.MySql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    public SalleModel(int id, String nom_salle) {
        Id = id;
        this.nom_salle = nom_salle;
    }
    public SalleModel(int id) {
        Id = id;
    }
}
