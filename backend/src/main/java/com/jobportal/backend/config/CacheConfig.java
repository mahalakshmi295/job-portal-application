package com.jobportal.backend.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for caching external API responses
 * Reduces API calls and improves performance
 */
@Configuration
@EnableCaching
public class CacheConfig {
    // Caching is automatically configured via application.properties
    // Cache names: jobSearchCache, jobDetailsCache, locationCache
    // Default TTL: 5 minutes
}
