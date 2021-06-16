package tn.isetsf.bpointage.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCardDash {
    private int nbUser;
    private int nbAdmin;
    private int nbResponsable;
    private int nbEnseignant;
    private int nbBlock;
    private int nbSalle;
    private int nbPreRattrapage;
    private int nbRattrapage;
}
