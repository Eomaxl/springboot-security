package com.example.springbootsecurity.service;

import com.example.springbootsecurity.auth.AuthenticationRequest;
import com.example.springbootsecurity.auth.AuthenticationResponse;
import com.example.springbootsecurity.model.Role;
import com.example.springbootsecurity.model.User;
import com.example.springbootsecurity.repository.RoleCustomRepo;
import com.example.springbootsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RoleCustomRepo roleCustomRepo;
    private final UserService userService;

    public ResponseEntity<?> authenticate(AuthenticationRequest authenticationRequest){
        try{
            User user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow(() -> new NoSuchElementException("user not found"));
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),authenticationRequest.getPassword()));
            List<Role> role = null;
            if(user != null){
                role = roleCustomRepo.getRole(user);
            }
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            Set<Role> set = new HashSet<>();
            role.stream().forEach(c->{set.add(new Role(c.getName()));
                authorities.add(new SimpleGrantedAuthority(c.getName()));
            });
            user.setRoles(set);
            set.stream().forEach(i->authorities.add(new SimpleGrantedAuthority(i.getName())));
            var jwtAccessToken = jwtService.generateToken(user,authorities);
            var jwtRefreshToken = jwtService.generateRefreshToken(user,authorities);
            return ResponseEntity.ok(AuthenticationResponse.builder().accessToken(jwtAccessToken).refreshToken(jwtRefreshToken).email(user.getEmail()).userName(user.getUsername()).build());
        } catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong at the backend");
        }
    }
}
