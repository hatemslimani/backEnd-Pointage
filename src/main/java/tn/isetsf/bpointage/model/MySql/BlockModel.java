package tn.isetsf.bpointage.model.MySql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="block")
public class BlockModel {
    @Id
    private int id;
    private String nom_block;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_departement")
    private DepartementModel departement;
    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "idBlock1",referencedColumnName = "id")
    private List<EtageModel> etages=new ArrayList<>();
}
