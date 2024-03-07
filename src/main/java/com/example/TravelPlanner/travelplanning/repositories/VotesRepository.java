package com.example.TravelPlanner.travelplanning.repositories;

import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.travelplanning.entities.Vote;
import com.example.TravelPlanner.travelplanning.entities.Voting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface VotesRepository extends JpaRepository<Vote, Long> {

    Vote findByCreatorAndVoting(User creator, Voting voting);

    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Vote v WHERE" +
            " v.creator.id = :creatorId AND" +
            " v.voting.id = :votingId")
    boolean existsByCreatorIdAAndVotingId(@Param("creatorId") UUID creatorId, @Param("votingId") Long votingId);

}
