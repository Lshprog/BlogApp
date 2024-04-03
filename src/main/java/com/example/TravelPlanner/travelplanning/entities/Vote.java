package com.example.TravelPlanner.travelplanning.entities;

import com.example.TravelPlanner.auth.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "votes")
public class Vote implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    @NotNull
    private User creator;

    @ManyToOne
    @JoinColumn(name = "voting_id", referencedColumnName = "id")
    @ToString.Exclude
    @NotNull
    private Voting voting;

    @Column(name = "is_liked")
    @NotNull
    private Boolean isLiked;

}
