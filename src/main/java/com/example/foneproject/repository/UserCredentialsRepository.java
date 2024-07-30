package com.example.foneproject.repository;

import com.example.foneproject.entity.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, String> {
    boolean existsByEmail(String email);

    Optional<UserCredentials> findByEmail(String email);

    @Modifying
    @Query("update UserCredentials uc set uc.email= ?2 where uc.email= ?1")
    void updateEmail(String currentEmail, String newEmail);

    @Modifying
    @Query("update UserCredentials uc set uc.password= ?2 where uc.email= ?1")
    void updatePassword(String email, String password);

    @Modifying
    @Query("update UserCredentials uc set uc.enabled= false where uc.email= ?1")
    void disableByEmail(String email);

    @Modifying
    @Query("update UserCredentials uc set uc.enabled= true where uc.email= ?1")
    void enableByEmail(String email);

    @Modifying
    @Query("update UserCredentials uc set uc.authorities= ?2 where uc.email= ?1")
    void assignRoleByEmail(String email, String authorities);
}
