package tn.isetsf.bpointage.model.SqlServer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="salle")
public class SalleModelSqlServer {
    @Id
    private int COD_salle;
    private String nom_salle;
    @ManyToOne
    @JoinColumn(name = "block1")
    private BlockModelSqlServer block;

    @ManyToOne
    @JoinColumn(name = "dep1")
    private DepartementModelSqlServer departement;
}
