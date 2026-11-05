package com.notzed.rahmaApp.program_service.dto;

import com.notzed.rahmaApp.program_service.entity.EnrollmentStatus;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramEnrollmentDto {

    private Long enrollmentId;

    private Long programId;

    private Long userId;

    private LocalDateTime enrolledAt;

    private EnrollmentStatus status;
}
