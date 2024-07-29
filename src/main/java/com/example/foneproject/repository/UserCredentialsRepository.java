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
    @Query("update UserCredentials uc set uc.email= ?1 where uc.email= ?2")
    void updateEmail(String newEmail, String currentEmail);

    @Modifying
    @Query("update UserCredentials uc set uc.password= ?1 where uc.email= ?2")
    void updatePassword(String password, String email);

    @Modifying
    @Query("update UserCredentials uc set uc.enabled= false where uc.email= ?1")
    void disableByEmail(String email);

    @Modifying
    @Query("update UserCredentials uc set uc.enabled= true where uc.email= ?1")
    void enableByEmail(String email);

    @Modifying
    @Query("update UserCredentials uc set uc.authorities= ?1 where uc.email= ?2")
    void assignRoleByEmail(String authorities, String email);
}
