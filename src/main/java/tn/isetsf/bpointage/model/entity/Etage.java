package tn.isetsf.bpointage.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.isetsf.bpointage.model.MySql.SalleModel;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Etage
{
    private int idEtage;
    private String NomBlock;
    private int idBlock;
    private String nomEtage;
    private List<SalleModel>salles;
}
