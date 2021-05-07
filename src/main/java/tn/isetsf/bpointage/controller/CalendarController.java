package tn.isetsf.bpointage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.isetsf.bpointage.model.MySql.AnneeUnviModel;
import tn.isetsf.bpointage.model.MySql.EventModel;
import tn.isetsf.bpointage.service.MySql.CalendarService;

@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8100"})
@RestController
@RequestMapping("/calendar")
public class CalendarController {
    @Autowired
    private CalendarService calendarService;
    @PostMapping("/add/{idAnneeUniv}")
    public void addEvent(@RequestBody EventModel c,@PathVariable int idAnneeUniv)
    {
        AnneeUnviModel annee=calendarService.getAnneeUnivById(idAnneeUniv);
        if(annee==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"essayee d'ajouter un annee universitaire ");
        }
        c.setAnneeUnvi(annee);
        calendarService.Add(c);
    }
    @GetMapping("/delete/{id}")
    public void deleteEvent(@PathVariable int id)
    {
        EventModel event =calendarService.getEventById(id);
        if(event==null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Événement introuvable!! ");
        }
        calendarService.delete(id);
    }
    @GetMapping("/")
    public AnneeUnviModel getAll()
    {
        java.sql.Date toDay= new java.sql.Date(new java.util.Date().getTime());
        return calendarService.getAll(toDay);
    }
    @PostMapping("/addAnneeUnvi")
    public void addAnneeUnvi(@RequestBody AnneeUnviModel annee)
    {
        AnneeUnviModel  anneeUnvi=calendarService.getAll(annee.getStart());
        if (anneeUnvi != null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Deja exist !! ");
        }
        else
        {
            calendarService.addAnneUnvi(annee);
        }
    }
}
