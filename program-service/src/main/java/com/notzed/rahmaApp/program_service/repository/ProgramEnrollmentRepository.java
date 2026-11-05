package com.notzed.rahmaApp.program_service.repository;

import com.notzed.rahmaApp.program_service.entity.EnrollmentStatus;
import com.notzed.rahmaApp.program_service.entity.ProgramEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramEnrollmentRepository extends JpaRepository<ProgramEnrollment, Long> {

    boolean existsByProgram_ProgramIdAndUserIdAndStatus(Long programId, Long userId, EnrollmentStatus status);

    boolean existsByProgram_ProgramIdAndStatus(Long programId, EnrollmentStatus status);

    long countByProgram_ProgramIdAndStatus(Long programId, EnrollmentStatus status);

    List<ProgramEnrollment> findByProgram_ProgramId(Long programId);

    List<ProgramEnrollment> findByUserId(Long userId);
}