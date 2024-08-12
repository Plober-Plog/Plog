package com.plog.backend.domain.user.repository;

import com.plog.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findUserBySearchId(String searchId);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndPassword(String email, String password);

    String findSearchIdByUserId(Long userId);
}
