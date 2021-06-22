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
@Table(name="etage")
public class EtageModel{
    @Id
    @GeneratedValue
    private int id;
    private String NomEtage;
    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "id_etage",referencedColumnName = "id")
    private List<SalleModel> Salles=new ArrayList<>();
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "idBlock1")
    private BlockModel block;
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;
}
