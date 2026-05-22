package com.project.security.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rights")
@Getter
@Setter
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
}
