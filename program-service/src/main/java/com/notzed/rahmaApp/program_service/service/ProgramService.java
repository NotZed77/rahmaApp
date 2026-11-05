package com.notzed.rahmaApp.program_service.service;

import com.notzed.rahmaApp.program_service.dto.ProgramDto;
import com.notzed.rahmaApp.program_service.dto.ProgramEnrollmentDto;
import com.notzed.rahmaApp.program_service.entity.EnrollmentStatus;
import com.notzed.rahmaApp.program_service.entity.Program;
import com.notzed.rahmaApp.program_service.entity.ProgramEnrollment;
import com.notzed.rahmaApp.program_service.entity.ProgramStatus;
import com.notzed.rahmaApp.program_service.repository.ProgramEnrollmentRepository;
import com.notzed.rahmaApp.program_service.repository.ProgramRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProgramService {

    private final ProgramRepository programRepository;
    private final ProgramEnrollmentRepository programEnrollmentRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public ProgramDto getProgramById(Long programId){
        Program program = findProgram(programId);
        return modelMapper.map(program, ProgramDto.class);
    }

    @Transactional(readOnly = true)
    public List<ProgramDto> getAllPrograms() {
        return programRepository.findAll()
                .stream()
                .map(program -> modelMapper.map(program, ProgramDto.class))
                .toList();
    }

    public ProgramDto createProgram(ProgramDto programDto) {
        validateProgram(programDto);
        Program program = modelMapper.map(programDto, Program.class);
        program.setProgramId(null);
        Program savedProgram = programRepository.save(program);

        log.info("Created program with ID: {}", savedProgram.getProgramId());

        return modelMapper.map(savedProgram, ProgramDto.class);
    }

    public ProgramDto updateProgramById(Long programId, ProgramDto programDto) {
        Program program = findProgram(programId);
        validateProgram(programDto);

        program.setTitle(programDto.getTitle());
        program.setDescription(programDto.getDescription());
        program.setCategory(programDto.getCategory());
        program.setInstructorName(programDto.getInstructorName());
        program.setStartDate(programDto.getStartDate());
        program.setEndDate(programDto.getEndDate());
        program.setStartTime(programDto.getStartTime());
        program.setEndTime(programDto.getEndTime());
        program.setLocation(programDto.getLocation());
        program.setCapacity(programDto.getCapacity());
        program.setRegistrationRequired(programDto.getRegistrationRequired());
        program.setStatus(programDto.getStatus());

        Program updatedProgram = programRepository.save(program);

        log.info("Updated program with ID: {}", programId);
        return modelMapper.map(updatedProgram, ProgramDto.class);
    }

    public void deleteProgramById(Long programId) {

        Program program = findProgram(programId);

        if (programEnrollmentRepository.existsByProgram_ProgramIdAndStatus(programId, EnrollmentStatus.REGISTERED)) {
            throw new IllegalStateException("Cannot delete a program with active enrollments");
        }
        programRepository.delete(program);
        log.info("Deleted program with ID: {}", programId);
    }

    public ProgramEnrollmentDto enrollInProgram(ProgramEnrollmentDto enrollmentDto) {
        Long programId = enrollmentDto.getProgramId();
        Long userId = enrollmentDto.getUserId();

        if (programId == null || userId == null) {
            throw new IllegalArgumentException("Program ID and User ID are required");
        }

        Program program = findProgram(programId);

        if (Boolean.FALSE.equals(program.getRegistrationRequired())) {
            throw new IllegalStateException(
                    "Registration is not required for this program"
            );
        }

        if (program.getStatus() != ProgramStatus.ACTIVE) {
            throw new IllegalStateException("Registration is not available for this program");
        }

        if (program.getStartDate() != null && program.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Registration is closed for this program");
        }

        boolean alreadyEnrolled = programEnrollmentRepository
                .existsByProgram_ProgramIdAndUserIdAndStatus(programId, userId, EnrollmentStatus.REGISTERED);

        if (alreadyEnrolled) {
            throw new IllegalStateException("User is already enrolled in this program");
        }

        Integer capacity = program.getCapacity();
        if (capacity != null) {
            long enrolledCount = programEnrollmentRepository
                    .countByProgram_ProgramIdAndStatus(programId, EnrollmentStatus.REGISTERED);

            if (enrolledCount >= capacity) {
                throw new IllegalStateException("Program has reached its maximum capacity");
            }
        }

        ProgramEnrollment enrollment = new ProgramEnrollment();

        enrollment.setProgram(program);
        enrollment.setUserId(userId);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment.setStatus(EnrollmentStatus.REGISTERED);

        ProgramEnrollment savedEnrollment =
                programEnrollmentRepository.save(enrollment);

        log.info(
                "User {} enrolled in program {}",
                userId,
                programId
        );

        return modelMapper.map(
                savedEnrollment,
                ProgramEnrollmentDto.class
        );
    }

    public ProgramEnrollmentDto cancelEnrollment(Long enrollmentId, Long userId, boolean isAdmin) {

        ProgramEnrollment enrollment = programEnrollmentRepository.findById(enrollmentId)
                        .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        if (!isAdmin && !enrollment.getUserId().equals(userId)) {
            throw new AccessDeniedException("You cannot cancel another user's enrollment");
        }

        if (enrollment.getStatus() == EnrollmentStatus.CANCELLED) {
            throw new IllegalStateException("Enrollment is already cancelled");
        }

        enrollment.setStatus(EnrollmentStatus.CANCELLED);
        ProgramEnrollment savedEnrollment = programEnrollmentRepository.save(enrollment);

        return modelMapper.map(savedEnrollment, ProgramEnrollmentDto.class
        );
    }

    @Transactional(readOnly = true)
    public List<ProgramEnrollmentDto> getEnrollmentsByProgram(Long programId) {

        findProgram(programId);
        return programEnrollmentRepository.findByProgram_ProgramId(programId).stream()
                .map(enrollment -> modelMapper.map(enrollment, ProgramEnrollmentDto.class)).toList();
    }

    @Transactional(readOnly = true)
    public List<ProgramEnrollmentDto> getEnrollmentsByUser(Long userId) {
        return programEnrollmentRepository
                .findByUserId(userId).stream()
                .map(enrollment -> modelMapper.map(enrollment, ProgramEnrollmentDto.class))
                .toList();
    }

    public ProgramDto updateProgramStatus(Long programId, ProgramStatus status) {

        Program program = findProgram(programId);

        if (status == null) {
            throw new IllegalArgumentException("Program status cannot be null");
        }

        program.setStatus(status);
        Program updatedProgram = programRepository.save(program);
        return modelMapper.map(updatedProgram, ProgramDto.class);
    }


    private Program findProgram(Long programId) {
        return programRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Program not found with ID: " + programId));
    }

    private void validateProgram(ProgramDto programDto) {
        if (programDto.getTitle() == null || programDto.getTitle().isBlank()) {
            throw new IllegalArgumentException("Program title is required");
        }

        if (programDto.getStartDate() != null && programDto.getEndDate() != null && programDto.getEndDate().isBefore(programDto.getStartDate())) {
            throw new IllegalArgumentException("Program end date cannot be before start date");
        }

        if (programDto.getStartTime() != null && programDto.getEndTime() != null && programDto.getEndTime().isBefore(programDto.getStartTime())) {
            throw new IllegalArgumentException("Program end time cannot be before start time");
        }

        if (programDto.getCapacity() != null && programDto.getCapacity() <= 0) {
            throw new IllegalArgumentException("Program capacity must be greater than zero");
        }
    }
}


