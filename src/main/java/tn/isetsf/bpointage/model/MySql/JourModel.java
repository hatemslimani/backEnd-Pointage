package tn.isetsf.bpointage.model.MySql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "Jour")
public class JourModel {
    @Id
    @GeneratedValue
    private int cod_Jour;
    private String nom_Jour;
}
