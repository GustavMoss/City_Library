package com.example.citylibrary.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    // the "custom" method for finding a user by email. Although it seems like JPA still does most of the work, since I never wrote any logic or query
    Users findByEmail(@NotBlank(message = "Email cannot be empty") @Email(message = "Email must be valid email") String email);

    @Query("SELECT u.member_number FROM Users u ORDER BY u.user_id DESC LIMIT 1")
    String findLastMemberNumber();

}
