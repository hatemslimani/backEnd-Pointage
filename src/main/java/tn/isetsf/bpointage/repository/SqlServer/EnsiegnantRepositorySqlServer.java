package tn.isetsf.bpointage.repository.SqlServer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.isetsf.bpointage.model.SqlServer.EnsiegnantModelSqlServer;

import java.util.List;

@Repository
public interface EnsiegnantRepositorySqlServer extends JpaRepository<EnsiegnantModelSqlServer,Integer> {
    @Query("select new tn.isetsf.bpointage.model.SqlServer.EnsiegnantModelSqlServer(e.COD_Enseig,e.nom_Ensi ) from EnsiegnantModelSqlServer e where e.nom_Ensi like %:name% and e.departement.COD_dep=:idDep order by e.nom_Ensi asc ")
    List<EnsiegnantModelSqlServer> findAllbyNameByDep(@Param("name") String name,int idDep);
    @Query("select e.COD_Enseig from EnsiegnantModelSqlServer e where e.departement.COD_dep=:id")
    List<Integer> findAllBydepartement(int id);
    @Query("select e.nom_Ensi from EnsiegnantModelSqlServer  e where e.COD_Enseig=:idEnsiegnant")
    String getNameEnsie(int idEnsiegnant);
    @Query("select new tn.isetsf.bpointage.model.SqlServer.EnsiegnantModelSqlServer(e.COD_Enseig,e.nom_Ensi,e.Email ) from EnsiegnantModelSqlServer e where  e.nom_Ensi like %:name% and e.COD_Enseig not in :enseignantSignIn  and e.departement.COD_dep <>1 ")
    List<EnsiegnantModelSqlServer> getEnseignantsNotSignIn(String name, List<Integer> enseignantSignIn);
    @Query("select new tn.isetsf.bpointage.model.SqlServer.EnsiegnantModelSqlServer(e.COD_Enseig,e.nom_Ensi,e.Email ) from EnsiegnantModelSqlServer e where  e.nom_Ensi like %:name% and e.departement.COD_dep <>1 ")
    List<EnsiegnantModelSqlServer> getEnseignantsNotSignIn(String name);
    @Query("select new tn.isetsf.bpointage.model.SqlServer.EnsiegnantModelSqlServer(e.COD_Enseig,e.nom_Ensi ) from EnsiegnantModelSqlServer e where e.nom_Ensi like %:name% order by e.nom_Ensi asc ")
    List<EnsiegnantModelSqlServer> findAllbyName(String name);
}
