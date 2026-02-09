package com.jobportal.backend.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "resume_analysis_results")
public class ResumeAnalysisResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double atsScore;
    private Double keywordMatchScore;
    private Double formatScore;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "section_completeness", joinColumns = @JoinColumn(name = "analysis_id"))
    @MapKeyColumn(name = "section_name")
    @Column(name = "is_complete")
    private Map<String, Boolean> sectionCompleteness = new HashMap<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "extracted_skills", joinColumns = @JoinColumn(name = "analysis_id"))
    @Column(name = "skill")
    private List<String> extractedSkills = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "missing_skills", joinColumns = @JoinColumn(name = "analysis_id"))
    @Column(name = "skill")
    private List<String> missingSkills = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "weak_sections", joinColumns = @JoinColumn(name = "analysis_id"))
    @Column(name = "section")
    private List<String> weakSections = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "keyword_gaps", joinColumns = @JoinColumn(name = "analysis_id"))
    @Column(name = "keyword")
    private List<String> keywordGaps = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "suggestions", joinColumns = @JoinColumn(name = "analysis_id"))
    @Column(name = "suggestion", length = 1000)
    private List<String> suggestions = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String semanticFeedback;

    // Constructors
    public ResumeAnalysisResult() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAtsScore() {
        return atsScore;
    }

    public void setAtsScore(Double atsScore) {
        this.atsScore = atsScore;
    }

    public Double getKeywordMatchScore() {
        return keywordMatchScore;
    }

    public void setKeywordMatchScore(Double keywordMatchScore) {
        this.keywordMatchScore = keywordMatchScore;
    }

    public Double getFormatScore() {
        return formatScore;
    }

    public void setFormatScore(Double formatScore) {
        this.formatScore = formatScore;
    }

    public Map<String, Boolean> getSectionCompleteness() {
        return sectionCompleteness;
    }

    public void setSectionCompleteness(Map<String, Boolean> sectionCompleteness) {
        this.sectionCompleteness = sectionCompleteness;
    }

    public List<String> getExtractedSkills() {
        return extractedSkills;
    }

    public void setExtractedSkills(List<String> extractedSkills) {
        this.extractedSkills = extractedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

    public List<String> getWeakSections() {
        return weakSections;
    }

    public void setWeakSections(List<String> weakSections) {
        this.weakSections = weakSections;
    }

    public List<String> getKeywordGaps() {
        return keywordGaps;
    }

    public void setKeywordGaps(List<String> keywordGaps) {
        this.keywordGaps = keywordGaps;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public String getSemanticFeedback() {
        return semanticFeedback;
    }

    public void setSemanticFeedback(String semanticFeedback) {
        this.semanticFeedback = semanticFeedback;
    }
}
