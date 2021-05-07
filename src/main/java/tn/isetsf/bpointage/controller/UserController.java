package tn.isetsf.bpointage.controller;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.server.ResponseStatusException;
import tn.isetsf.bpointage.model.MySql.AuthRequest;
import tn.isetsf.bpointage.model.MySql.User;
import tn.isetsf.bpointage.model.entity.UserEntity;
import tn.isetsf.bpointage.service.MySql.UserService;
import tn.isetsf.bpointage.util.JwtUtil;

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
    @GetMapping("/")
    public String welcome() {
        return "Welcome !!";
    }


    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
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
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Password invalid");
            }
        }
        return jwtUtil.generateToken(authRequest.getUserName());
    }
    @PostMapping("/createUser")
    public void createUser(@RequestBody User u)
    {
        if(u.getUserName().isEmpty() || u.getNom().isEmpty() || u.getPassword().isEmpty()||
            u.getPrenom().isEmpty() || u.getRole().isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"tous les informations est obligatoire");
        }
        else
        {
            if(userService.getUserByEmail(u.getUserName())!=null)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email deja utiliser");
            }else
            {
                EmailValidator validator = EmailValidator.getInstance();
                if (!validator.isValid(u.getUserName()))
                {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email invalide");
                }
                userService.CreateUser(u);
            }
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
    public User getUser()
    {
        return userService.getUser(577);
    }
    @PostMapping("/ChangeInfoPer")
    public void ChangeInfoPersonneller(@RequestBody User u)
    {
        User user=userService.getUser(u.getId());
        user.setNom(u.getNom());
        user.setPrenom(u.getPrenom());
        userService.updateUser(user);

    }
    @PostMapping("/ChangePassword")
    public void changerPassword(@RequestBody UserEntity u)
    {
        if (u.getPassword()=="" || u.getId()==0 || u.getNewPassword()=="")
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"tous les champs obligatoire");
        }
        User user=userService.getUser(u.getId());
        if (!passwordEncoder.matches(u.getPassword(), user.getPassword()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"s'il te plaît verifiez Mot de passe actuel ");
        }

        user.setPassword(passwordEncoder.encode(u.getNewPassword()));
        userService.updateUser(user);
        throw new ResponseStatusException(HttpStatus.OK,"Mot de passe changer");
    }
    @PostMapping("/ChangeEmail")
    public void changerEmail(@RequestBody UserEntity u)
    {
        if(u.getUserName()==null ||  u.getPassword()==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"tous les informations est obligatoire");
        }
        else
        {
            if(userService.getUserByEmail(u.getUserName())!=null)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email deja utiliser");
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
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"s'il te plaît verifiez Mot de passe actuel ");
                }
                if (user != null)
                {
                    user.setUserName(u.getUserName());
                    userService.updateUser(user);
                    throw new ResponseStatusException(HttpStatus.OK,"Email changer");
                }
            }

        }
    }


}
