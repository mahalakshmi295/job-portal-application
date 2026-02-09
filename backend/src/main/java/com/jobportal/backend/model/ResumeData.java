package com.jobportal.backend.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "resume_data")
public class ResumeData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "resume_personal_info", joinColumns = @JoinColumn(name = "resume_data_id"))
    @MapKeyColumn(name = "info_key")
    @Column(name = "info_value")
    private Map<String, String> personalInfo = new HashMap<>();

    @Column(columnDefinition = "TEXT")
    private String professionalSummary;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "resume_data_id")
    private List<Education> education = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "resume_data_id")
    private List<Experience> experience = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "resume_skills", joinColumns = @JoinColumn(name = "resume_data_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "resume_data_id")
    private List<Project> projects = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "resume_certifications", joinColumns = @JoinColumn(name = "resume_data_id"))
    @Column(name = "certification")
    private List<String> certifications = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "resume_interests", joinColumns = @JoinColumn(name = "resume_data_id"))
    @Column(name = "interest")
    private List<String> areasOfInterest = new ArrayList<>();

    // Constructors
    public ResumeData() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, String> getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(Map<String, String> personalInfo) {
        this.personalInfo = personalInfo;
    }

    public String getProfessionalSummary() {
        return professionalSummary;
    }

    public void setProfessionalSummary(String professionalSummary) {
        this.professionalSummary = professionalSummary;
    }

    public List<Education> getEducation() {
        return education;
    }

    public void setEducation(List<Education> education) {
        this.education = education;
    }

    public List<Experience> getExperience() {
        return experience;
    }

    public void setExperience(List<Experience> experience) {
        this.experience = experience;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<String> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<String> certifications) {
        this.certifications = certifications;
    }

    public List<String> getAreasOfInterest() {
        return areasOfInterest;
    }

    public void setAreasOfInterest(List<String> areasOfInterest) {
        this.areasOfInterest = areasOfInterest;
    }
}
