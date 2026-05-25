package com.project.security.repository;

import com.project.security.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserInfo, String> {
    @Query("""
        select distinct u
        from UserInfo u
        left join fetch u.role r
        left join fetch r.rights
        where u.username = :username
    """)
    Optional<UserInfo> findByUsername(@Param("username") String username);
}
