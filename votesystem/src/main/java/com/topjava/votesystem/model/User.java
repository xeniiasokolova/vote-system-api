package com.topjava.votesystem.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    public static final int START_SEQ_USER = 100000;
    @Id
    @SequenceGenerator(name = "global_seq_users", sequenceName = "global_seq_users", allocationSize = 1, initialValue = START_SEQ_USER)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq_users")
    private Long id;

    @Column(name = "firstname")
    @NotBlank
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    private String username;

    @Size(min = 5, max = 12)
    @NotBlank
    private String password;

    @NotBlank
    private String passwordConfirm;

    @Email(message = "Некорректный адрес электронной почты")
    @NotBlank(message = "Поле 'Email' обязательно для заполнения")
    @Column(name = "email", unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private Role userRole;

    private boolean locked = false;
    private boolean enabled = true;
    private LocalDateTime registered = LocalDateTime.now();


    public User(String firstName, String lastName, String username,
                String email, String password,
                Role userRole) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.userRole = userRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userRole.name());
        return Collections.singletonList(authority);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

    public boolean isNew() {
        return this.id == null;
    }

}
