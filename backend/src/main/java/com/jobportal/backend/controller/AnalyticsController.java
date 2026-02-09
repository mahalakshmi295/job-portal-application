package com.jobportal.backend.controller;

import com.jobportal.backend.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.*;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "http://localhost:3000")
public class AnalyticsController {
    
    @Autowired
    private AnalyticsService analyticsService;
    
    /**
     * Get comprehensive user analytics
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserAnalytics(
            @PathVariable String userId,
            @RequestParam(defaultValue = "month") String period) {
        try {
            Map<String, Object> analytics = analyticsService.getUserAnalytics(userId, period);
            return ResponseEntity.ok(analytics);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch user analytics: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * Get job market analytics
     */
    @GetMapping("/job-market")
    public ResponseEntity<Map<String, Object>> getJobMarketAnalytics(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location) {
        try {
            Map<String, Object> analytics = analyticsService.getJobMarketAnalytics(category, location);
            return ResponseEntity.ok(analytics);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch job market analytics: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * Get application funnel analytics
     */
    @GetMapping("/funnel/{userId}")
    public ResponseEntity<Map<String, Object>> getApplicationFunnelAnalytics(@PathVariable String userId) {
        try {
            Map<String, Object> funnel = analyticsService.getApplicationFunnelAnalytics(userId);
            return ResponseEntity.ok(funnel);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch funnel analytics: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * Get skill analytics
     */
    @PostMapping("/skills")
    public ResponseEntity<Map<String, Object>> getSkillAnalytics(@RequestBody Map<String, Object> payload) {
        try {
            @SuppressWarnings("unchecked")
            List<String> userSkills = (List<String>) payload.getOrDefault("skills", 
                                       payload.getOrDefault("userSkills", new ArrayList<>()));
            
            Map<String, Object> analytics = analyticsService.getSkillAnalytics(userSkills);
            return ResponseEntity.ok(analytics);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch skill analytics: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * Get platform-wide analytics (admin view)
     */
    @GetMapping("/platform")
    public ResponseEntity<Map<String, Object>> getPlatformAnalytics() {
        try {
            Map<String, Object> analytics = new HashMap<>();
            
            // Platform statistics
            Map<String, Object> platformStats = new HashMap<>();
            platformStats.put("totalUsers", 12547);
            platformStats.put("activeUsers", 8932);
            platformStats.put("totalJobs", 3421);
            platformStats.put("activeJobs", 1876);
            platformStats.put("totalApplications", 45623);
            platformStats.put("successfulPlacements", 2341);
            platformStats.put("avgTimeToHire", "23 days");
            platformStats.put("platformSatisfaction", 4.6);
            
            // User growth
            List<Map<String, Object>> userGrowth = new ArrayList<>();
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun"};
            int[] values = {8500, 9200, 9800, 10500, 11400, 12547};
            for (int i = 0; i < months.length; i++) {
                Map<String, Object> point = new HashMap<>();
                point.put("month", months[i]);
                point.put("users", values[i]);
                userGrowth.add(point);
            }
            
            // Top categories
            Map<String, Integer> topCategories = new HashMap<>();
            topCategories.put("Software Development", 1245);
            topCategories.put("Data Science", 678);
            topCategories.put("DevOps", 456);
            topCategories.put("Cloud Engineering", 389);
            topCategories.put("Product Management", 234);
            
            // Industry distribution
            Map<String, Integer> industryDistribution = new HashMap<>();
            industryDistribution.put("Technology", 42);
            industryDistribution.put("Finance", 23);
            industryDistribution.put("Healthcare", 15);
            industryDistribution.put("E-commerce", 12);
            industryDistribution.put("Other", 8);
            
            // Top recruiters
            List<Map<String, Object>> topRecruiters = new ArrayList<>();
            topRecruiters.add(createRecruiterData("Google", 145, 4.8));
            topRecruiters.add(createRecruiterData("Amazon", 132, 4.6));
            topRecruiters.add(createRecruiterData("Microsoft", 128, 4.7));
            topRecruiters.add(createRecruiterData("Meta", 98, 4.5));
            topRecruiters.add(createRecruiterData("Apple", 87, 4.9));
            
            analytics.put("platformStats", platformStats);
            analytics.put("userGrowth", userGrowth);
            analytics.put("topCategories", topCategories);
            analytics.put("industryDistribution", industryDistribution);
            analytics.put("topRecruiters", topRecruiters);
            
            return ResponseEntity.ok(analytics);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch platform analytics: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * Get real-time statistics
     */
    @GetMapping("/realtime")
    public ResponseEntity<Map<String, Object>> getRealtimeStats() {
        try {
            Map<String, Object> realtime = new HashMap<>();
            
            realtime.put("activeUsers", 342);
            realtime.put("jobsPostedToday", 23);
            realtime.put("applicationsToday", 156);
            realtime.put("interviewsScheduled", 12);
            realtime.put("onlineRecruiters", 45);
            realtime.put("recentActivity", Arrays.asList(
                "New job posted: Senior Java Developer",
                "Application submitted for Data Scientist role",
                "Interview scheduled with Tech Corp",
                "Profile updated by John Doe",
                "New message from Innovation Labs"
            ));
            realtime.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(realtime);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch realtime stats: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * Get dashboard summary
     */
    @GetMapping("/dashboard/{userId}")
    public ResponseEntity<Map<String, Object>> getDashboardSummary(@PathVariable String userId) {
        try {
            Map<String, Object> dashboard = new HashMap<>();
            
            // Quick stats
            Map<String, Object> quickStats = new HashMap<>();
            quickStats.put("profileViews", 234);
            quickStats.put("jobMatches", 42);
            quickStats.put("applications", 15);
            quickStats.put("interviews", 3);
            
            // Recent activity
            List<String> recentActivity = Arrays.asList(
                "Applied to Senior Developer role at Tech Corp",
                "Your resume was viewed 5 times today",
                "New job match: Full Stack Developer",
                "Interview reminder: Tomorrow at 2 PM"
            );
            
            // Action items
            List<Map<String, String>> actionItems = new ArrayList<>();
            actionItems.add(createActionItem("Update your resume", "high", "/resume-score"));
            actionItems.add(createActionItem("Complete skill gap analysis", "medium", "/skill-gap"));
            actionItems.add(createActionItem("Respond to recruiter message", "high", "/messages"));
            actionItems.add(createActionItem("Prepare for upcoming interview", "urgent", "/interviews"));
            
            // Performance score
            Map<String, Object> performanceScore = new HashMap<>();
            performanceScore.put("overall", 78);
            performanceScore.put("profile", 85);
            performanceScore.put("resume", 87);
            performanceScore.put("activity", 72);
            performanceScore.put("response", 65);
            
            dashboard.put("quickStats", quickStats);
            dashboard.put("recentActivity", recentActivity);
            dashboard.put("actionItems", actionItems);
            dashboard.put("performanceScore", performanceScore);
            dashboard.put("lastLogin", "2 hours ago");
            
            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch dashboard: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    // Helper methods
    
    private Map<String, Object> createRecruiterData(String name, int jobs, double rating) {
        Map<String, Object> recruiter = new HashMap<>();
        recruiter.put("name", name);
        recruiter.put("activeJobs", jobs);
        recruiter.put("rating", rating);
        return recruiter;
    }
    
    private Map<String, String> createActionItem(String text, String priority, String link) {
        Map<String, String> item = new HashMap<>();
        item.put("text", text);
        item.put("priority", priority);
        item.put("link", link);
        return item;
    }
}
