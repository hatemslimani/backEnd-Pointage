package tn.isetsf.bpointage.service.MySql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.MySql.ControlModel;
import tn.isetsf.bpointage.model.MySql.JourModel;
import tn.isetsf.bpointage.model.MySql.SeanceModel;
import tn.isetsf.bpointage.repository.MySql.ControlRepository;

import java.sql.Date;

@Service
public class ControlService {
    @Autowired
    private ControlRepository controlRepository;

    public ControlModel findd(int id) {
        return controlRepository.findById(id).orElse(null);
    }

    public ControlModel addControl(ControlModel c)
    {
        return controlRepository.save(c);
    }


    public ControlModel getByBlockByDateBySeanceByJour(int idBlock, Date toDay, int idSeance, int cod_jour) {
        return controlRepository.getByBlockByDateBySeanceByJour(idBlock,toDay,idSeance,cod_jour);
    }

    public ControlModel getByVerification(int idBlock, Date toDay, int seance) {
        return controlRepository.getByVerification(idBlock,toDay,seance);
    }
}
