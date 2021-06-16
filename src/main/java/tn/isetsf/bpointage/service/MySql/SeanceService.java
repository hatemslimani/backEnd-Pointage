package tn.isetsf.bpointage.service.MySql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.MySql.SeanceModel;
import tn.isetsf.bpointage.repository.MySql.SeanceRepository;
import tn.isetsf.bpointage.model.SqlServer.SeanceModelSqlServer;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

@Service
public class SeanceService {
    @Autowired
    private SeanceRepository seanceRepository;
    public List<SeanceModel> getAll()
    {
        return seanceRepository.getAll();
    }
    public void storeSeance(SeanceModelSqlServer s)
    {
        SeanceModel seance=new SeanceModel();
        System.out.println(s.getCOD_senace());
        seance.setId(s.getCOD_senace());
        seance.setNom_Seance(s.getNom_Seance());
        seance.setDuree(s.getDuree());
        seance.setDebutSeance(toTime(s.getNom_Seance().substring(0,s.getNom_Seance().indexOf("A")).replace(".",":").trim()+":00"));
        seance.setFinSeance(toTime(s.getNom_Seance().substring(s.getNom_Seance().indexOf("A")+1).replace(".",":").trim()+":00"));
        seanceRepository.save(seance);
    }
    private Time toTime(String s)
    {
        if(s.length()==5)
        {
            s=s+":00";
        }
        return Time.valueOf(s);
    }
    public SeanceModel getSeanceByTime(Time t)
    {
        return seanceRepository.findSeanceByTime(t);
    }

    public List<Integer> getSeancesByTime(Time time) {
        return seanceRepository.getSeancesByTime(time);
    }
}
