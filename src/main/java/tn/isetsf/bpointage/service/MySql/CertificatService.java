package tn.isetsf.bpointage.service.MySql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.MySql.CertificatModel;
import tn.isetsf.bpointage.repository.MySql.CertificatRepository;

import java.util.List;

@Service
public class CertificatService {
    @Autowired
    private CertificatRepository certificatRepository;

    public void add(CertificatModel c) {
        certificatRepository.save(c);
    }

    public List<CertificatModel> getAllByDepartement(List<Integer> listIdEnsei) {
        return certificatRepository.getAllByDepartement(listIdEnsei);
    }
    public List<CertificatModel> getAll() {
        return certificatRepository.findAll();
    }

    public void deleteById(int idCerti) {
        certificatRepository.deleteById(idCerti);
    }
    public CertificatModel getById(int id)
    {
        return certificatRepository.findById(id).orElse(null);
    }

    public void save(CertificatModel certificatModel) {
        certificatRepository.save(certificatModel);
    }
}
