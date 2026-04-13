package com.project.security.repository;


import com.project.security.entity.Information;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfromationRepository extends JpaRepository<Information, Long> {
}
