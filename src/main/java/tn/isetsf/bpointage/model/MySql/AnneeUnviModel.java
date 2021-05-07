package tn.isetsf.bpointage.model.MySql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="anneeUnvi")
public class AnneeUnviModel {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int idAnneeUnvi;
    private String title;
    private Date start;
    private Date end;
    private int numSemstre1=1;
    private Date startSemstre1;
    private Date endSemestre1;
    private int numSestre2=2;
    private Date startSemstre2;
    private Date endSemestre2;
    @OneToMany
    @JoinColumn(name = "idAnneeUnvi",referencedColumnName = "idAnneeUnvi")
    private List<EventModel> Events=new ArrayList<>();
}
