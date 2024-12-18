package com.plog.realtime.domain.user.repository;

import com.plog.realtime.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findUserBySearchId(String searchId);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndPassword(String email, String password);

    User findByUserId(Long userId);
}
