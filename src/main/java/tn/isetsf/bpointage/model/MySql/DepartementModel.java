package tn.isetsf.bpointage.model.MySql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="departement")
public class DepartementModel {
    @Id
    private int id;
    private String nom_dapartement;
    @OneToMany
    @JoinColumn(name = "id_departement",referencedColumnName = "id")
    @JsonIgnore
    List<BlockModel>blocks=new ArrayList<>();
}
