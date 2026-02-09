package com.jobportal.backend.model;

import jakarta.persistence.*;

@Entity
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String skillsRequired;
    private String location;
    private String company;
    private boolean active = true;

    // Constructors
    public Job() {}

    public Job(String title, String description, String skillsRequired, String location, String company) {
        this.title = title;
        this.description = description;
        this.skillsRequired = skillsRequired;
        this.location = location;
        this.company = company;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getSkillsRequired() { return skillsRequired; }
    public void setSkillsRequired(String skillsRequired) { this.skillsRequired = skillsRequired; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
