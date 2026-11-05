package com.notzed.rahmaApp.program_service.dto;

import com.notzed.rahmaApp.program_service.entity.Program;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassDto {
    private Long id;
    private LocalDate schedule;
    private Integer capacity;
    private String instructor;
    private Program program;
}
