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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Année universitaire n'existe pas");
        }
        c.setAnneeUnvi(annee);
        calendarService.Add(c);
        throw new ResponseStatusException(HttpStatus.OK,"Vacance ajoutée avec succès");
    }
    @GetMapping("/delete/{id}")
    public void deleteEvent(@PathVariable int id)
    {
        EventModel event =calendarService.getEventById(id);
        if(event==null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Vacance introuvable!");
        }
        calendarService.delete(id);
        throw new ResponseStatusException(HttpStatus.OK,"Vacance supprimée avec succès ");
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Année universitaire existe déja");
        }
        else
        {
            calendarService.addAnneUnvi(annee);
            throw new ResponseStatusException(HttpStatus.OK,"Année universitaire ajoutée avec succès ");
        }
    }
    @PostMapping("/updateAnneUnvi")
    public void updateAnneeUnvi(@RequestBody AnneeUnviModel anneeUnviModel)
    {
        AnneeUnviModel annee=calendarService.getAnneeUnivById(anneeUnviModel.getIdAnneeUnvi());
        if (annee==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Année universitaire n'existe pas ");
        }
        annee.setStart(anneeUnviModel.getStart());
        annee.setEnd(anneeUnviModel.getEnd());
        annee.setStartSemstre1(anneeUnviModel.getStartSemstre1());
        annee.setEndSemestre1(anneeUnviModel.getEndSemestre1());
        annee.setStartSemstre2(anneeUnviModel.getStartSemstre2());
        annee.setEndSemestre2(anneeUnviModel.getEndSemestre2());
        calendarService.updateAnneeUnvi(annee);
        throw new ResponseStatusException(HttpStatus.OK,"Année universitaire modifiée avec succès ");
    }
    @GetMapping("/getEvent/{id}")
    public EventModel getEvent(@PathVariable int id)
    {
        return calendarService.getEventById(id);
    }
    @PostMapping("/updateEvent")
    public void updateEvent(@RequestBody EventModel eventModel)
    {
        EventModel event=calendarService.getEventById(eventModel.getId());
        if (event==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Vacance introuvable! ");
        }
        event.setTitle(eventModel.getTitle());
        event.setStart(eventModel.getStart());
        event.setEnd(eventModel.getEnd());
        calendarService.updateEvent(event);
        throw new ResponseStatusException(HttpStatus.OK,"Vacance modifiée avec succès ");
    }
}
