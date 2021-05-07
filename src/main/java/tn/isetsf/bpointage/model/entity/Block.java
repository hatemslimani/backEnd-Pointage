package tn.isetsf.bpointage.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Block {
    private int id;
    private String nom_block;
    private boolean isPointee;
}
