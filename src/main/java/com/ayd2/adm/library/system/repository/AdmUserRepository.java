package com.ayd2.adm.library.system.repository;

import com.ayd2.adm.library.system.model.AdmUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdmUserRepository extends JpaRepository<AdmUser, Long> {

    Optional<AdmUser> findByEmail(String email);

    @Query(value = "SELECT user FROM AdmUser user WHERE user.email = :email AND user.userId <> :userId")
    Optional<AdmUser> findDuplicatedByEmailAndNotId(@Param("email") String email, @Param("userId") Long userId);
}
