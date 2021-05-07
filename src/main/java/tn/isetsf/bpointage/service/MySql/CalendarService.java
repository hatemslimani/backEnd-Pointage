package tn.isetsf.bpointage.service.MySql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.isetsf.bpointage.model.MySql.AnneeUnviModel;
import tn.isetsf.bpointage.model.MySql.EventModel;
import tn.isetsf.bpointage.repository.MySql.AnneeUnviRepository;
import tn.isetsf.bpointage.repository.MySql.EventRepository;

import java.sql.Date;
import java.util.List;

@Service
public class CalendarService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private AnneeUnviRepository anneeUnviRepository;
    public EventModel getEventById(int id)
    {
        return eventRepository.findById(id).orElse(null);
    }
    public void Add(EventModel c)
    {
        eventRepository.save(c);
    }
    public void delete(int id)
    {
        eventRepository.deleteById(id);
    }
    public AnneeUnviModel getAll(Date toDay)
    {
        return anneeUnviRepository.getAll(toDay);
    }
    public AnneeUnviModel addAnneUnvi(AnneeUnviModel anneeUnviModel)
    {
        return anneeUnviRepository.save(anneeUnviModel);
    }
    public AnneeUnviModel getAnneeUnivById(int id)
    {
        return anneeUnviRepository.findById(id).orElse(null);
    }

    public List<EventModel> getEventsByDate(Date toDay) {
        return eventRepository.getEventsByDate(toDay);
    }
}
