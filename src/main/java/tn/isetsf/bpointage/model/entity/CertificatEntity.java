package tn.isetsf.bpointage.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificatEntity {
    private int idCertificat;
    private Date start;
    private Date end;
    private String img;
    private int status;
    private int idEnseignant;
    private String nomEnseignant;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public CertificatEntity(int idCertificat, Date start, Date end, int status, int idEnseignant) {
        this.idCertificat = idCertificat;
        this.start = start;
        this.end = end;
        this.status = status;
        this.idEnseignant = idEnseignant;
    }

    public CertificatEntity(int idCertificat, Date start, Date end, String img, int status, String nomEnseignant, Timestamp createdAt) {
        this.idCertificat = idCertificat;
        this.start = start;
        this.end = end;
        this.img = img;
        this.status = status;
        this.nomEnseignant = nomEnseignant;
        this.createdAt = createdAt;
    }
}

