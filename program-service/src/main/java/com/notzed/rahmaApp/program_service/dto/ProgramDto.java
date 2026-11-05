package com.notzed.rahmaApp.program_service.dto;

import com.notzed.rahmaApp.program_service.entity.ProgramCategory;
import com.notzed.rahmaApp.program_service.entity.ProgramStatus;
import jakarta.persistence.EnumType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public class ProgramDto {

        private Long programId;

        @NotBlank
        private String title;

        private String description;

        private ProgramCategory category;

        private String instructorName;

        private LocalDate startDate;

        private LocalDate endDate;

        private LocalTime startTime;

        private LocalTime endTime;

        private String location;

        @Positive
        private Integer capacity;

        private Boolean registrationRequired;

        private ProgramStatus status;
    }

