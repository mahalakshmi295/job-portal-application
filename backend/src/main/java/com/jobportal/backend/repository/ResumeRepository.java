package com.jobportal.backend.repository;

import com.jobportal.backend.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByUserId(String userId);
    Optional<Resume> findByUserIdAndId(String userId, Long id);
}
