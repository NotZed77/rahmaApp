package com.notzed.rahmaApp.program_service.repository;

import com.notzed.rahmaApp.program_service.dto.ProgramDto;
import com.notzed.rahmaApp.program_service.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramRepository extends JpaRepository<Program, Long> {

}
