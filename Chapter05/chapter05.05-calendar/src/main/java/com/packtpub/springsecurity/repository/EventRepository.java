package com.packtpub.springsecurity.repository;

import java.util.List;

import com.packtpub.springsecurity.domain.Event;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface EventRepository extends MongoRepository<Event, Integer> {

    @Query("{'owner.id' : ?0}")
    List<Event> findByUser(Integer name);

}
