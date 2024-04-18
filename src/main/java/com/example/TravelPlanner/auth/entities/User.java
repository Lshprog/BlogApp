package com.example.TravelPlanner.auth.entities;

import com.example.TravelPlanner.travelplanning.entities.Event;
import com.example.TravelPlanner.travelplanning.entities.TravelPlan;
import com.example.TravelPlanner.travelplanning.entities.UserPlan;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.apache.commons.lang3.builder.HashCodeExclude;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Setter
@Getter
@ToString
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", indexes = {@Index(name = "idx_username", columnList = "username", unique = true)})
public class User implements Serializable {

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
    @ToString.Exclude
    private String password;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner", orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    private List<TravelPlan> travelPlans = new ArrayList<>();

    @OneToMany(mappedBy = "creator")
    @ToString.Exclude
    @Builder.Default
    private List<Event> events = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @Builder.Default
    private Set<UserPlan> userPlanRoles = new HashSet<>();

}
