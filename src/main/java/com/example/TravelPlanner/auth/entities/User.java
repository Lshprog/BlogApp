package com.example.TravelPlanner.auth.entities;

import com.example.TravelPlanner.travelplanning.entities.Event;
import com.example.TravelPlanner.travelplanning.entities.UserPlanRoles;
import com.example.TravelPlanner.travelplanning.entities.TravelPlan;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false, unique = true)
    private UUID id;

    @Column(length = 12, columnDefinition = "varchar(20)", nullable = false, unique = true)
    @NotBlank(message = "Username is mandatory")
    private String username;

    @Column(columnDefinition = "varchar(36)", nullable = false, unique = true)
    @Email(message = "Must be in email format")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Column(name = "password_hash",nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String profileImageUrl; // connect aws bucket and then save profile pics there

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner", orphanRemoval = true)
    private List<TravelPlan> travelPlans = new ArrayList<>();

    @OneToMany(mappedBy = "creator")
    private List<Event> events = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private Set<UserPlanRoles> userPlanRoles = new HashSet<>();
}
