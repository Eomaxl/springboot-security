package com.example.springbootsecurity.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@Table(name="Users")
public class User implements UserDetails {

    @PrePersist
    protected void onCreate(){
        this.createdOn = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedOn = new Date(System.currentTimeMillis());
    }
    @Id
    private String userId;
    private String userName;
    private String email;
    private String password;
    private String mobileNumber;
    @ManyToMany
    @JoinTable(name = "user_role",joinColumns = @JoinColumn(name="userId"),inverseJoinColumns = @JoinColumn(name="roleId"))
    private Set<Role> roles = new HashSet<>();
    private Date createdOn;
    private Date updatedOn;

    public User(String mobileNumber, String userName, String email, String password, Set<Role> roles){
        this.mobileNumber = mobileNumber;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.stream().forEach(i -> authorities.add(new SimpleGrantedAuthority(i.getName())));
        return List.of(new SimpleGrantedAuthority(authorities.toString()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
