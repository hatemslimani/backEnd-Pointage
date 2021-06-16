package tn.isetsf.bpointage.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tn.isetsf.bpointage.filter.JwtFilter;
import tn.isetsf.bpointage.service.MySql.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final String[] endPoint={
            "user/authenticate"
    };
    private final String[] endPointAdmin={
            "/createUser",
            //"/auth/**", "/user/createUser", "/user/getUsers", "/user/deleteUser","/user/updatePassword",
            "/departement/**",
            "/block/**",
            "/salle/**",

            "/ensiegnement/**",
            "/group/**",

    };
    private final String[] endPointResponsable={
           // "/auth/**", "/user/**",
            //"/departement/**",
            "/salle/**",

            "/ensiegnement/**",
            "/group/**",
            "/gestionPre/**",

    };
    private final String[] endPointController={
            "/pointage/**",
    };
    private final String[] endPointEnseignant={
            "/enseignant/**"
    };
    @Autowired
    private UserService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests().antMatchers("/user/authenticate","/enseignant/**","/calendar/**","/ensiegnant/**","/pointage/**","/statistic/**","/certificat/**").permitAll().
                antMatchers(endPointAdmin).hasAnyRole("ADMIN").
                antMatchers(endPointResponsable).hasAnyRole("RESPONSABLE").
                antMatchers(endPointEnseignant).hasAnyRole("ENSEIGNANT").
               // antMatchers(endPointController).hasAnyRole("CONTROLLER").
                antMatchers("/report/**").hasAnyRole("RESPONSABLE","ADMIN").
                antMatchers("/notification/**").hasAnyRole("RESPONSABLE","ADMIN","ENSEIGNANT").
                anyRequest().authenticated().
                and().exceptionHandling().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:4200","http://localhost:8100");
            }
        };
    }
}
