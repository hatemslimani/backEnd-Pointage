package tn.isetsf.bpointage.model.SqlServer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "block")
public class BlockModelSqlServer {
    @Id
    private int code_block;
    private String nom_block;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "block1",referencedColumnName = "code_block")
    private Set<SalleModelSqlServer> salles;
}
