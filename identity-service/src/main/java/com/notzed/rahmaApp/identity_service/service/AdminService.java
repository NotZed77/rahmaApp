package com.notzed.rahmaApp.identity_service.service;

import com.notzed.rahmaApp.identity_service.dto.IdentityDto;
import com.notzed.rahmaApp.identity_service.entity.Identity;
import com.notzed.rahmaApp.identity_service.entity.Role;
import com.notzed.rahmaApp.identity_service.exception.BadRequestException;
import com.notzed.rahmaApp.identity_service.exception.ResourceNotFoundException;
import com.notzed.rahmaApp.identity_service.repository.IdentityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {

    private final ModelMapper modelMapper;
    private final IdentityRepository identityRepository;

    @Transactional
    public IdentityDto promoteUser(Long userId) {

        Identity identity = identityRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (identity.getRole() == Role.ADMIN)
            throw new BadRequestException("User is already an ADMIN");

        if (identity.getRole() == Role.STAFF) {
            identity.setRole(Role.ADMIN);
            log.info("Promoted STAFF to ADMIN, user ID: {}", userId);
        } else {
            identity.setRole(Role.STAFF);
            log.info("Promoted USER to STAFF, user ID: {}", userId);
        }

        Identity savedIdentity = identityRepository.save(identity);

        return modelMapper.map(savedIdentity, IdentityDto.class);
    }
}
