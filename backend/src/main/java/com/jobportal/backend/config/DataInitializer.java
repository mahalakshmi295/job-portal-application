package com.jobportal.backend.config;

import com.jobportal.backend.model.Job;
import com.jobportal.backend.model.User;
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, JobRepository jobRepository) {
        return args -> {
            // Initialize sample users
            if (userRepository.count() == 0) {
                userRepository.save(new User("admin", "admin123", "admin@jobportal.com", "ADMIN"));
                userRepository.save(new User("recruiter1", "recruiter123", "recruiter@company.com", "RECRUITER"));
                userRepository.save(new User("candidate1", "candidate123", "candidate@email.com", "CANDIDATE"));
                System.out.println("✅ Sample users initialized");
            }
            
            // Initialize sample jobs
            if (jobRepository.count() == 0) {
                jobRepository.save(new Job(
                    "Senior Java Developer",
                    "Looking for experienced Java developer with Spring Boot expertise",
                    "Java, Spring Boot, MySQL, REST APIs, Microservices",
                    "New York, NY",
                    "Tech Corp Inc"
                ));
                
                jobRepository.save(new Job(
                    "Frontend React Developer",
                    "Build modern web applications using React and TypeScript",
                    "React, TypeScript, HTML, CSS, JavaScript, Redux",
                    "San Francisco, CA",
                    "WebDev Solutions"
                ));
                
                jobRepository.save(new Job(
                    "Full Stack Developer",
                    "Work on both frontend and backend technologies",
                    "Java, Spring Boot, React, Node.js, MongoDB, AWS",
                    "Austin, TX",
                    "Innovation Labs"
                ));
                
                jobRepository.save(new Job(
                    "Data Scientist",
                    "Analyze data and build ML models",
                    "Python, Machine Learning, TensorFlow, SQL, Statistics",
                    "Boston, MA",
                    "AI Analytics Corp"
                ));
                
                jobRepository.save(new Job(
                    "DevOps Engineer",
                    "Manage cloud infrastructure and CI/CD pipelines",
                    "Docker, Kubernetes, AWS, Jenkins, Terraform, Linux",
                    "Seattle, WA",
                    "Cloud Services Ltd"
                ));
                
                System.out.println("✅ Sample jobs initialized");
            }
            
            System.out.println("🚀 Job Portal Backend is ready!");
            System.out.println("📊 Database initialized with sample data");
            System.out.println("🌐 Access H2 Console at: http://localhost:8080/h2-console");
            System.out.println("💡 API Health Check: http://localhost:8080/api/health");
        };
    }
}
