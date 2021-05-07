package tn.isetsf.bpointage.service.MySql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.MySql.ProblemPointageModel;
import tn.isetsf.bpointage.repository.MySql.ProblemPointageRepository;

import java.util.List;

@Service
public class ProblemPointageService {
    @Autowired
    private ProblemPointageRepository problemPointageRepository;
    public ProblemPointageModel storeProblemPointage(ProblemPointageModel p)
    {
        return problemPointageRepository.save(p);
    }
    public List<ProblemPointageModel> getAllProblemPointage()
    {
        return problemPointageRepository.findAll();
    }

    public Integer getNbNewProblemPointage() {
        return problemPointageRepository.getNbNewProblemPointage();
    }
}
