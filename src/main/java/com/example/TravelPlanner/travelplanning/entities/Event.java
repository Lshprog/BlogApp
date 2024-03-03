package com.example.TravelPlanner.travelplanning.entities;

import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.travelplanning.common.enums.PlaceStatus;
import com.example.TravelPlanner.travelplanning.common.pojos.Location;
import com.example.TravelPlanner.common.utils.MapperUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "events")
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User creator;

    @Column(name = "title")
    private String title;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "travel_plan_id", referencedColumnName = "id")
    private TravelPlan travelPlan;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    private PlaceStatus placeStatus;

    @Column(name = "location")
    private String location;

    @Transient
    private Location loc;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "voting_id", referencedColumnName = "id")
    private Voting voting;

}
