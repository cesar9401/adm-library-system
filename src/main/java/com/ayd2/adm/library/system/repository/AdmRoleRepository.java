package com.ayd2.adm.library.system.repository;

import com.ayd2.adm.library.system.model.AdmRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface AdmRoleRepository extends JpaRepository<AdmRole, Long> {

    @Query(value = "SELECT role FROM AdmUserRole userRole INNER JOIN userRole.role role WHERE userRole.user.userId = :userId")
    Set<AdmRole> findAllRolesByUserId(@Param("userId") Long userId);

    AdmRole findByRoleId(Long roleId);
}

