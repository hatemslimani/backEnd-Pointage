package tn.isetsf.bpointage.model.MySql;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="control")
public class ControlModel {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int idControl;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateControl;
    private Time tempsControl;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "idblockk")
    private BlockModel block;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "idJour")
    private JourModel jour;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "idSeance")
    private SeanceModel seance;
    @OneToMany(cascade=CascadeType.REMOVE)
    @JoinColumn(name = "id_Control",referencedColumnName = "idControl")
    @JsonIgnore
    List<PointageModel>pointages=new ArrayList<>();
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

}
