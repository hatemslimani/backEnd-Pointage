package tn.isetsf.bpointage.model.MySql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "event")
public class EventModel {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String title;
    private Date start;
    private Date end;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "idAnneeUnvi")
    private AnneeUnviModel anneeUnvi;

}
