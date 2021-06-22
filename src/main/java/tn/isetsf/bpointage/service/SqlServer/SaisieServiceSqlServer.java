package tn.isetsf.bpointage.service.SqlServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.MySql.SalleModel;
import tn.isetsf.bpointage.model.MySql.SeanceAbsenceModel;
import tn.isetsf.bpointage.model.MySql.SeanceDenseignement;
import tn.isetsf.bpointage.model.SqlServer.SaisieModelSqlServer;
import tn.isetsf.bpointage.repository.SqlServer.SaisieRepositorySqlServer;

import java.util.List;

@Service
public class SaisieServiceSqlServer {
    @Autowired
    private SaisieRepositorySqlServer saisieRepositorySqlServer;
    public List<SeanceDenseignement> getSaisenceParEnsiegnant(int id)
    {
        return  saisieRepositorySqlServer.findAllByEnsiegnant(id);
    }
    public List<SeanceAbsenceModel> getSeanceAbsence(String nomEnsiegnant, int idGroup,int idJour) {
        return saisieRepositorySqlServer.findAllSeanceAbsence(nomEnsiegnant,idGroup,idJour);
    }

    public List<SalleModel> getFreeSalle(int  idJour, int idSeance) {
        return saisieRepositorySqlServer.findAllFreeSalle(idJour,idSeance);
    }

    public SaisieModelSqlServer verfierSeanceEnsiegnenment(int idSalle, int cod_jour, List<Integer> listSeance,int year,int semestre) {
        return saisieRepositorySqlServer.verfierSeanceEnsiegnenment(idSalle,cod_jour,listSeance,year,semestre);
    }
    public SaisieModelSqlServer getSeanceById(int id)
    {
        return saisieRepositorySqlServer.findById(id).orElse(null);
    }
    public List<SaisieModelSqlServer> getSeanceDenseignement(int idJour,int idEnsie,int year,int semestre) {
        return saisieRepositorySqlServer.getSeanceDenseignement(idJour,idEnsie,year,semestre);
    }

    public List<Integer> getIdSeanceGroupe(int idJour, int idGroup) {
        return saisieRepositorySqlServer.getIdSeanceGroupe(idJour,idGroup);
    }

    public List<Integer> getIdSeanceEnseignant(int idJour, int idEnseignant) {
        return saisieRepositorySqlServer.getIdSeanceEnseignant(idJour,idEnseignant);
    }

    public Double getDuree(int idSeanceAbsence) {
        return saisieRepositorySqlServer.getDuree(idSeanceAbsence);
    }

    public List<SalleModel> getFreeSallePre(int idJour, List<Integer> idSeance, List<Integer> idSallePre, List<Integer> idSalleRatt) {
        return saisieRepositorySqlServer.getFreeSallePre(idJour,idSeance,idSallePre,idSalleRatt);
    }
}
