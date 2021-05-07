package tn.isetsf.bpointage.service.MySql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.MySql.AbsenceModel;
import tn.isetsf.bpointage.repository.MySql.AbsenceRepository;

import java.util.List;

@Service
public class AbsenceService {
    @Autowired
    private AbsenceRepository absenceRepository;
    public AbsenceModel storeAbsence(AbsenceModel absence)
    {
        return absenceRepository.save(absence);
    }
    public AbsenceModel getAbsenceById(int id)
    {
        return absenceRepository.findById(id).orElse(null);
    }

    public List<AbsenceModel> getAbcensesNonVerifier(int idEnseignant) {
        return absenceRepository.getAbcensesNonVerifier(idEnseignant);
    }
}
