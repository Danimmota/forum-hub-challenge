package com.api.forumHub.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

     UserDetails findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findEntityByEmail(@Param("email") String email);

    Optional<User> findUserByEmail (String authenticatedUserEmail);

}
