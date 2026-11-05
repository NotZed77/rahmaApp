package com.notzed.rahmaApp.program_service.controller;

import com.notzed.rahmaApp.program_service.dto.ProgramDto;
import com.notzed.rahmaApp.program_service.dto.ProgramEnrollmentDto;
import com.notzed.rahmaApp.program_service.entity.ProgramStatus;
import com.notzed.rahmaApp.program_service.service.ProgramService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class ProgramController {

    private final ProgramService programService;

    @PostMapping
    public ResponseEntity<ProgramDto> createProgram(@Valid @RequestBody ProgramDto programDto) {
        ProgramDto createdProgram = programService.createProgram(programDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProgram);
    }

    @GetMapping("/{programId}")
    public ResponseEntity<ProgramDto> getProgramById(@PathVariable Long programId) {
        ProgramDto program = programService.getProgramById(programId);
        return ResponseEntity.ok(program);
    }

    @GetMapping
    public ResponseEntity<List<ProgramDto>> getAllPrograms() {
        List<ProgramDto> programs = programService.getAllPrograms();
        return ResponseEntity.ok(programs);
    }

    @PutMapping("/{programId}")
    public ResponseEntity<ProgramDto> updateProgramById(@PathVariable Long programId, @Valid @RequestBody ProgramDto programDto) {
        ProgramDto updatedProgram = programService.updateProgramById(programId, programDto);
        return ResponseEntity.ok(updatedProgram);
    }

    @DeleteMapping("/{programId}")
    public ResponseEntity<Void> deleteProgramById(@PathVariable Long programId) {
        programService.deleteProgramById(programId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/enrollments")
    public ResponseEntity<ProgramEnrollmentDto> enrollInProgram(@Valid @RequestBody ProgramEnrollmentDto enrollmentDto,
                                                                Authentication authentication) {
        enrollmentDto.setUserId(Long.parseLong(authentication.getName()));
        ProgramEnrollmentDto enrollment = programService.enrollInProgram(enrollmentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
    }

    @PatchMapping("/enrollments/{enrollmentId}/cancel")
    public ResponseEntity<ProgramEnrollmentDto> cancelEnrollment(@PathVariable Long enrollmentId, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> Objects.equals(authority.getAuthority(), "ROLE_ADMIN"));

        ProgramEnrollmentDto enrollment = programService.cancelEnrollment(enrollmentId, userId, isAdmin);
        return ResponseEntity.ok(enrollment);
    }

    @GetMapping("/{programId}/enrollments")
    public ResponseEntity<List<ProgramEnrollmentDto>> getEnrollmentsByProgram(@PathVariable Long programId) {
        List<ProgramEnrollmentDto> enrollments = programService.getEnrollmentsByProgram(programId);
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/users/{userId}/enrollments")
    public ResponseEntity<List<ProgramEnrollmentDto>> getEnrollmentsByUser(@PathVariable Long userId) {
        List<ProgramEnrollmentDto> enrollments = programService.getEnrollmentsByUser(userId);
        return ResponseEntity.ok(enrollments);
    }

    @PatchMapping("/{programId}/status")
    public ResponseEntity<ProgramDto> updateProgramStatus(@PathVariable Long programId, @RequestParam ProgramStatus status) {
        ProgramDto updatedProgram = programService.updateProgramStatus(programId, status);
        return ResponseEntity.ok(updatedProgram);
    }
}



