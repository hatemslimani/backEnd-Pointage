package tn.isetsf.bpointage.service.MySql;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import tn.isetsf.bpointage.model.MySql.User;
import tn.isetsf.bpointage.repository.MySql.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmail(username);
        List<GrantedAuthority> grantedAuthorities=new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+user.getRole().toUpperCase()));
        return new org.springframework.security.core.userdetails.User
                (user.getUserName(),
                        user.getPassword(),
                        grantedAuthorities);
    }
    private PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
    public User CreateUser(User u)
    {
       u.setPassword(passwordEncoder().encode(u.getPassword()));
        return repository.save(u);
    }
    public List<User> getUsers()
    {
        return repository.findAll();
    }
    public void deleteUser(int id)
    {
        repository.deleteById(id);
    }
    public User updatePassword(User user)
    {
        User u=repository.findById(user.getId()).orElse(null);
        if (!u.equals(null))
        {
            u.setPassword(passwordEncoder().encode(user.getPassword()));
            repository.save(u);
        }
        return u;
    }
    public User getUserByEmail(String email)
    {
        return repository.findByEmail(email);
    }
    public User getUser(int id)
    {
        return repository.findById(id).orElse(null);
    }
    public User updateUser(User u)
    {
        return repository.save(u);
    }
}
