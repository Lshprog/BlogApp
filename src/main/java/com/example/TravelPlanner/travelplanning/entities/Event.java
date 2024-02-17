package com.example.TravelPlanner.travelplanning.entities;

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
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "places")
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(name = "location", columnDefinition = "jsonb")
    private String location;

    @Transient
    private Location loc;

    public void setEventLocation(Location newLocation) {
        this.loc = newLocation;
        this.location = MapperUtil.convertPojoToJson(newLocation);
    }

    public Location getEventLocation() {
        if (this.loc == null && this.location != null) {
            this.loc = MapperUtil.convertJsonToPojo(this.location, Location.class);
        }
        return this.loc;
    }
}
