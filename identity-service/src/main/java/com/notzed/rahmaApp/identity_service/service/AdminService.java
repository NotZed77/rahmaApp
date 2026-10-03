package com.notzed.rahmaApp.identity_service.service;

import com.notzed.rahmaApp.identity_service.dto.IdentityDto;
import com.notzed.rahmaApp.identity_service.entity.Identity;
import com.notzed.rahmaApp.identity_service.entity.Role;
import com.notzed.rahmaApp.identity_service.repository.IdentityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {
    private final ModelMapper modelMapper;

    private final IdentityRepository identityRepository;

    public IdentityDto promoteUser(Long userId){
        Identity identity = identityRepository.findById(userId).
                orElseThrow(() -> new RuntimeException("User not found with ID: "+ userId));
        if(identity.getRole().equals(Role.ADMIN)){
            throw new RuntimeException("User cannot be promoted");
        }
        else if(identity.getRole().equals(Role.STAFF)){
            log.info("Promoting user from staff to admin");
            identity.setRole(Role.ADMIN);
        }
        else{
            log.info("Promoting user from user to staff");
            identity.setRole(Role.STAFF);
        }
        Identity savedIdentity = identityRepository.save(identity);
        return modelMapper.map(savedIdentity, IdentityDto.class);
    }
}