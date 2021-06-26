package tn.isetsf.bpointage.controller;

import net.sf.jasperreports.engine.JRException;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.sql.Date;
import java.util.List;

import org.springframework.web.server.ResponseStatusException;
import tn.isetsf.bpointage.model.MySql.AuthRequest;
import tn.isetsf.bpointage.model.MySql.User;
import tn.isetsf.bpointage.model.SqlServer.EnsiegnantModelSqlServer;
import tn.isetsf.bpointage.model.entity.UserEntity;
import tn.isetsf.bpointage.repository.MySql.DepartementRepository;
import tn.isetsf.bpointage.service.MySql.ReportService;
import tn.isetsf.bpointage.service.MySql.UserService;
import tn.isetsf.bpointage.service.SendEmailService;
import tn.isetsf.bpointage.service.SqlServer.EnsiegnantServiceSqlServer;
import tn.isetsf.bpointage.util.JwtUtil;

import javax.mail.MessagingException;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EnsiegnantServiceSqlServer ensiegnantServiceSqlServer;
    @Autowired
    private DepartementRepository departementRepository;
    @Autowired
    private SendEmailService sendEmailService;
    @PostMapping("/authenticate")
    public UserEntity generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        User user = userService.getUserByEmail(authRequest.getUserName());
        if(user==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email n'exist pas");
        }else
        {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
                );
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Mot de passe invalid");
            }
        }
        UserEntity u=new UserEntity(user.getNom(),user.getPrenom(),user.getUserName(),user.getRole(),jwtUtil.generateToken(user.getUserName()),user.getFirstLogin());
        return u;
    }
    @PostMapping("/createUser")
    public void createUser(@RequestBody UserEntity u) throws MessagingException {
        User us=new User();

        if(u.getRole()==null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Tous les informations sont obligatoires");

        if (u.getRole().equals("ENSEIGNANT"))
        {
            if(u.getUserName()==null || u.getIdEnseignant() ==0  )
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Tous les informations sont obligatoires");
            }
            EnsiegnantModelSqlServer ensiegnantModelSqlServer =ensiegnantServiceSqlServer.getEnsiegnantById(u.getIdEnseignant());
            us.setNom(ensiegnantModelSqlServer.getNom_Ensi());
            us.setIdEnseignant(u.getIdEnseignant());
        }
        else
        {
            if(u.getUserName()==null || u.getNom()==null||
                    u.getPrenom()==null )
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Tous les informations sont obligatoires");
            }
            if (u.getRole().equals("RESPONSABLE"))
            {
                us.setDepartementt(departementRepository.findById(u.getIdDepartementt()).orElse(null));
            }
            us.setNom(u.getNom());
            us.setPrenom(u.getPrenom());
        }
            if(userService.getUserByEmail(u.getUserName())!=null)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email déja utilisé");
            }else
            {
                EmailValidator validator = EmailValidator.getInstance();
                if (!validator.isValid(u.getUserName()))
                {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email invalide");
                }
                us.setUserName(u.getUserName());
                String password = RandomStringUtils.randomAlphanumeric(10);
                us.setPassword(password);
                sendEmailService.sendPassword(password,u.getUserName());
                us.setRole(u.getRole());
                us.setFirstLogin(true);
                userService.CreateUser(us);
            }
    }
    @GetMapping("/getUsers")
    public List<User> getUsers()
    {
        return userService.getUsers();
    }
    @DeleteMapping("/deleteUser/{id}")
    public void deleteUser(@PathVariable int id)
    {
         this.userService.deleteUser(id);
    }
    @PostMapping("updatePassword")
    public User updatePassword(@RequestBody User u)
    {
        return userService.updatePassword(u);
    }
    @GetMapping("/getUser")
    public User getUser(@AuthenticationPrincipal UserDetails user)
    {
        return userService.getUserByEmail(user.getUsername());
    }
    @PostMapping("/ChangeInfoPer")
    public void ChangeInfoPersonneller(@RequestBody User u)
    {
        User user=userService.getUser(u.getId());
        user.setNom(u.getNom());
        user.setPrenom(u.getPrenom());
        userService.updateUser(user);
        throw new ResponseStatusException(HttpStatus.OK,"information changée");
    }
    @PostMapping("/ChangePassword")
    public void changerPassword(@RequestBody UserEntity u)
    {
        if (u.getPassword()=="" || u.getId()==0 || u.getNewPassword()=="")
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Tous les champs sont obligatoires");
        }
        User user=userService.getUser(u.getId());
        if (!passwordEncoder.matches(u.getPassword(), user.getPassword()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"S'il vous plaît verifiez votre mot de passe actuel ");
        }
        user.setPassword(passwordEncoder.encode(u.getNewPassword()));
        userService.updateUser(user);
        throw new ResponseStatusException(HttpStatus.OK,"Mot de passe est changé avec succès");
    }
    @PostMapping("/ChangeEmail")
    public String changerEmail(@RequestBody UserEntity u)
    {
        if(u.getUserName()==null ||  u.getPassword()==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Tous les informations sont obligatoires");
        }
        else
        {
            if(userService.getUserByEmail(u.getUserName())!=null)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email déja utilisé");
            }else
            {

                EmailValidator validator = EmailValidator.getInstance();
                if (!validator.isValid(u.getUserName()))
                {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email invalide");
                }
                User user=userService.getUser(u.getId());
                if (!passwordEncoder.matches(u.getPassword(), user.getPassword()))
                {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"S'il vous plaît verifiez votre mot de passe actuel ");
                }
                if (user != null)
                {
                    user.setUserName(u.getUserName());
                    userService.updateUser(user);
                    return jwtUtil.generateToken(u.getUserName());
                }
            }

        }
        return null;
    }
    @GetMapping("/enseignant/{name}")
    public List<EnsiegnantModelSqlServer> getEnseignantsNotSignIn(@PathVariable String name)
    {
        return ensiegnantServiceSqlServer.getEnseignantsNotSignIn(name);
    }
    @PostMapping("/firstUpdatePassword")
    public void firstUpdatePassword(@RequestBody UserEntity u,@AuthenticationPrincipal UserDetails user1)
    {
        if (u.getPassword()=="")
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Tous les champs sont obligatoires");
        }
        User user=userService.getUserByEmail(user1.getUsername());
        user.setPassword(passwordEncoder.encode(u.getPassword()));
        user.setFirstLogin(false);
        userService.updateUser(user);
        throw new ResponseStatusException(HttpStatus.OK,"Mot de passe est changé avec succès");
    }
    @GetMapping("/isFisrtLogin")
    public boolean isFisrtLogin(@AuthenticationPrincipal UserDetails user1)
    {
        return userService.getUserByEmail(user1.getUsername()).getFirstLogin();
    }

}
