package com.jobportal.backend.config;

import com.jobportal.backend.model.Job;
import com.jobportal.backend.model.User;
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, JobRepository jobRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Initialize sample users with BCrypt hashed passwords
            if (userRepository.count() == 0) {
                userRepository.save(new User("admin", passwordEncoder.encode("admin123"), "admin@jobportal.com", "ADMIN"));
                userRepository.save(new User("recruiter1", passwordEncoder.encode("recruiter123"), "recruiter@company.com", "RECRUITER"));
                userRepository.save(new User("candidate1", passwordEncoder.encode("candidate123"), "candidate@email.com", "CANDIDATE"));
                logger.info("✅ Sample users initialized with secure password hashing");
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

                logger.info("✅ Sample jobs initialized");
            }

            logger.info("🚀 Job Portal Backend is ready!");
            logger.info("📊 Database initialized with sample data");
            logger.info("💡 API Health Check: http://localhost:8080/api/health");
        };
    }
}
