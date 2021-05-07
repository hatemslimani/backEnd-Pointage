package tn.isetsf.bpointage.model.SqlServer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="matiere")
public class MatiereModelSqlServer {
    @Id
    private int COD_matiere;
    private String abv;
    private String Nom_matiere;
    private Double Coef1;
    private Double coef2;
}
