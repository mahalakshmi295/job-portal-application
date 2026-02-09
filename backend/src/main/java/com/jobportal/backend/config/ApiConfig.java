package com.jobportal.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for external API integrations
 */
@Configuration
public class ApiConfig {
    
    // RapidAPI Configuration (Job Search)
    @Value("${rapidapi.key:}")
    private String rapidApiKey;
    
    @Value("${rapidapi.host:jsearch.p.rapidapi.com}")
    private String rapidApiHost;
    
    // OpenAI Configuration (Enhanced Chatbot)
    @Value("${openai.api.key:}")
    private String openAiApiKey;
    
    @Value("${openai.model:gpt-3.5-turbo}")
    private String openAiModel;
    
    // Affinda Configuration (Resume Parser)
    @Value("${affinda.api.token:}")
    private String affindaApiToken;
    
    // Geolocation Configuration
    @Value("${ipapi.base.url:https://ipapi.co}")
    private String ipapiBaseUrl;
    
    // API Endpoints
    public static final String RAPID_API_BASE_URL = "https://jsearch.p.rapidapi.com";
    public static final String OPENAI_API_BASE_URL = "https://api.openai.com/v1";
    public static final String AFFINDA_API_BASE_URL = "https://api.affinda.com/v3";
    
    // Getters
    public String getRapidApiKey() {
        return rapidApiKey;
    }
    
    public String getRapidApiHost() {
        return rapidApiHost;
    }
    
    public String getOpenAiApiKey() {
        return openAiApiKey;
    }
    
    public String getOpenAiModel() {
        return openAiModel;
    }
    
    public String getAffindaApiToken() {
        return affindaApiToken;
    }
    
    public String getIpapiBaseUrl() {
        return ipapiBaseUrl;
    }
    
    // Validation
    public boolean isRapidApiConfigured() {
        return rapidApiKey != null && !rapidApiKey.isEmpty() && !rapidApiKey.equals("your_rapidapi_key_here");
    }
    
    public boolean isOpenAiConfigured() {
        return openAiApiKey != null && !openAiApiKey.isEmpty() && openAiApiKey.startsWith("sk-");
    }
    
    public boolean isAffindaConfigured() {
        return affindaApiToken != null && !affindaApiToken.isEmpty();
    }
}
