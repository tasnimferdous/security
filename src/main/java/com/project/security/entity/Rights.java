package com.project.security.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rights")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rights {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "right_id")
    private int id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    private String description;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "rights")
    private Set<Roles> roles = new HashSet<>();
}
