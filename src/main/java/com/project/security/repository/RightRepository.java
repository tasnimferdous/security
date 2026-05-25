package com.project.security.repository;

import com.project.security.entity.Rights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RightRepository extends JpaRepository<Rights, Long> {
}
