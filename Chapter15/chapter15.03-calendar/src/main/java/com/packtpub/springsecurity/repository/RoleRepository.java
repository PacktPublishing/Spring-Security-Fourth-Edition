package com.packtpub.springsecurity.repository;

import com.packtpub.springsecurity.domain.Role;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface RoleRepository extends ReactiveMongoRepository<Role, Integer> {

}