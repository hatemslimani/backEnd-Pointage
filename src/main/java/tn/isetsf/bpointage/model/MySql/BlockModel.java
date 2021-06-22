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
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;
}
