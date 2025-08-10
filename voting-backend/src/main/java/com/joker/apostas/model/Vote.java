package com.joker.apostas.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String option; // e.g., "Team A", "Option 1"

    private LocalDate timestamp = LocalDate.now();

    
    public Vote() {}

    public Vote(String username, String option) {
        this.username = username;
        this.option = option;
    }

    // Getters and setters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getOption() { return option; }
    public LocalDate getTimestamp() { return timestamp; }

    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setOption(String option) { this.option = option; }
    public void setTimestamp(LocalDate timestamp) { this.timestamp = timestamp; }
}