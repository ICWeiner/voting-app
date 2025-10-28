package com.joker.apostas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "users")
public class User {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")  
    private Long id;

    @Column(name = "username", nullable = false)
    @Size(max = 100)
    private String username;

    @Column(name = "email", nullable = false)
    @Size(max = 100)
    private String email;

    @Column(name = "passwordhash", nullable = false)
    private String password; // BCrypt hashed

    //"USER" or "ADMIN"
    @Column(name = "role", nullable = false)
    private String role;

}