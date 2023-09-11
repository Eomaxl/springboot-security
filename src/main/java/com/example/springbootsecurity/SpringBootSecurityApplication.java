package com.example.springbootsecurity;

import com.example.springbootsecurity.model.Role;
import com.example.springbootsecurity.model.User;
import com.example.springbootsecurity.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;

@SpringBootApplication
@EnableWebSecurity
@EnableJpaRepositories
public class SpringBootSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityApplication.class, args);
    }
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    CommandLineRunner run(UserService userService){
        return args -> {
          userService.saveRole(new Role(null,"ROLE_USER","This is User"));
          userService.saveRole(new Role(null,"ROLE_ADMIN","This is admin"));
          userService.saveRole(new Role(null,"ROLE_MANAGER","This is manager"));
          userService.saveUser(new User("8527701888","sourav","somansin@cisco.com","pass",new HashSet<>()));
        };
    }
}
