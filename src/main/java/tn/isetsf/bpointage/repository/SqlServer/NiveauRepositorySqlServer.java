package tn.isetsf.bpointage.repository.SqlServer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.SqlServer.NiveauModelSqlServer;

import java.util.List;

@Repository
public interface NiveauRepositorySqlServer extends JpaRepository<NiveauModelSqlServer,Integer> {
    @Query("select new tn.isetsf.bpointage.model.SqlServer.NiveauModelSqlServer(n.COD_NIVEAU,n.Nom_niveau) from NiveauModelSqlServer n where n.COD_NIVEAU in (select s.niveau.COD_NIVEAU from SaisieModelSqlServer s where s.ensiegnant.COD_Enseig=:id and s.annee1=:year and s.semestre1=:semestre )")
    List<NiveauModelSqlServer> findNiveauByEnsieg(int id,int year, int semestre);
    @Query("select n.Nom_niveau from NiveauModelSqlServer n where n.COD_NIVEAU=:idNiveau")
    String getbyid(int idNiveau);
}
