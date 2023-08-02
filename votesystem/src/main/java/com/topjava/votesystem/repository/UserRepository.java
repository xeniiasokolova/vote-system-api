package com.topjava.votesystem.repository;

import com.topjava.votesystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    Optional<User> findByEmail(String email);

}
