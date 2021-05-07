package tn.isetsf.bpointage.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pointage {
    private int id_Salle;
    private Boolean occupee;
}
