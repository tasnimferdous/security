package com.project.security.repository;

import com.project.security.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer> {
    @Query("""
        select distinct r
        from Roles r
        left join fetch r.rights
        where r.id in :roleIds
    """)
    List<Roles> findAllRolesById(@Param("roleIds") Collection<Integer> rolesIds);
}
