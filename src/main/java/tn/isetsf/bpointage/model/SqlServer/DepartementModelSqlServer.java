package tn.isetsf.bpointage.model.SqlServer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="departement")
public class DepartementModelSqlServer {
    @Id
    private int COD_dep;
    private String nom_dep;
    private String nom_dep_ar;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "dep1",referencedColumnName = "COD_dep")
    private List<SalleModelSqlServer> salles=new ArrayList<>();
}
