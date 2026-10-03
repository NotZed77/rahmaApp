package com.notzed.rahmaApp.identity_service.client;

import com.notzed.rahmaApp.identity_service.dto.UserRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "USER-SERVICE")
public interface UserFeignClient {

    @PostMapping("/users/core")
    UserRequestDto createUser(@RequestBody UserRequestDto requestDto);
}
