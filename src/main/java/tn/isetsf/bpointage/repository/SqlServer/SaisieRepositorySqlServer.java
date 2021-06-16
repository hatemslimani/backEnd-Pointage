package tn.isetsf.bpointage.repository.SqlServer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.MySql.SalleModel;
import tn.isetsf.bpointage.model.MySql.SeanceAbsenceModel;
import tn.isetsf.bpointage.model.SqlServer.SaisieModelSqlServer;
import tn.isetsf.bpointage.model.MySql.SeanceDenseignement;

import java.util.List;

@Repository
public interface SaisieRepositorySqlServer extends JpaRepository<SaisieModelSqlServer,Integer> {

    @Query("select new tn.isetsf.bpointage.model.MySql.SeanceDenseignement(m.COD_matiere,m.abv,m.Nom_matiere,j.nom_Jour,n.Nom_niveau) from SaisieModelSqlServer s,MatiereModelSqlServer m,JourModelSqlServer j , NiveauModelSqlServer n where s.ensiegnant.COD_Enseig=:id and s.jour.cod_Jour=j.cod_Jour and s.matiere.COD_matiere=m.COD_matiere and s.niveau.COD_NIVEAU=n.COD_NIVEAU")
    List<SeanceDenseignement> findAllByEnsiegnant(int id);
    @Query("select new tn.isetsf.bpointage.model.MySql.SeanceAbsenceModel(s.matiere.Nom_matiere,s.num,s.seance.nom_Seance,s.jour.nom_Jour) from SaisieModelSqlServer s where s.ensiegnant.nom_Ensi like :nomEnsiegnant and s.niveau.COD_NIVEAU= :idGroup")
    List<SeanceAbsenceModel> findAllSeanceAbsence(String nomEnsiegnant, int idGroup);
    @Query("select new tn.isetsf.bpointage.model.MySql.SalleModel(c.COD_salle,c.nom_salle)  from SalleModelSqlServer c where c.COD_salle not in (select s.salle.COD_salle from SaisieModelSqlServer s where s.jour.cod_Jour=:idJour and s.seance.COD_senace=:idSeance) order by c.nom_salle")
    List<SalleModel> findAllFreeSalle(int idJour, int idSeance);
    @Query("select s from SaisieModelSqlServer s where s.salle.COD_salle=:idSalle and s.seance.COD_senace in :listSeance and s.jour.cod_Jour=:cod_jour and s.annee1=:year and s.semestre1=:semsetre")
    SaisieModelSqlServer verfierSeanceEnsiegnenment(int idSalle, int cod_jour, List<Integer> listSeance,int year,int semsetre);
    @Query("select s from SaisieModelSqlServer s where s.jour.cod_Jour=:idJour and s.ensiegnant.COD_Enseig =:idEnsie and s.annee1=:year and s.semestre1=:semestre")
    List<SaisieModelSqlServer> getSeanceDenseignement(int idJour,int idEnsie,int year,int semestre);
    @Query("select s.num from SaisieModelSqlServer s where s.seance.COD_senace =:idSeance and s.ensiegnant.COD_Enseig in :listeEnseignant")
    List<Integer> getIdSaisiesByDeapretemntBySeance(int idSeance,List<Integer> listeEnseignant);
    @Query("select s.num from SaisieModelSqlServer s where s.seance.COD_senace =:idSeance")
    List<Integer> getIdSaisiesBySeance(int idSeance);
}
