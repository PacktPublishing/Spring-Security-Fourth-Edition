package com.packtpub.springsecurity.repository;

import java.util.Date;
import java.util.List;

import com.packtpub.springsecurity.domain.PersistentLogin;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RememberMeTokenRepository extends JpaRepository<PersistentLogin, String> {

    PersistentLogin findBySeries(String series);
    List<PersistentLogin> findByUsername(String username);
    Iterable<PersistentLogin> findByLastUsedAfter(Date expiration);

}
