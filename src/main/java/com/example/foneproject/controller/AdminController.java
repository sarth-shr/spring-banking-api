package com.example.foneproject.controller;

import com.example.foneproject.dto.request.AuthoritiesRequestDTO;
import com.example.foneproject.entity.UserCredentials;
import com.example.foneproject.handler.JsonResponseHandler;
import com.example.foneproject.service.UserCredentialsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final ModelMapper modelMapper;
    private final JsonResponseHandler jsonResponseHandler;
    private final UserCredentialsService userCredentialsService;

    @PostMapping("/disable")
    public ResponseEntity<Map<String, Object>> disableUser(@RequestParam("email") String email) {
        userCredentialsService.disableUser(email);
        return jsonResponseHandler.get("User with email: " + email + " disabled", HttpStatus.OK.value(), HttpStatus.OK);
    }

    @PostMapping("/enable")
    public ResponseEntity<Map<String, Object>> enableUser(@RequestParam("email") String email) {
        userCredentialsService.enableUser(email);
        return jsonResponseHandler.get("User with email: " + email + " enabled", HttpStatus.OK.value(), HttpStatus.OK);
    }

    @PostMapping("/authorities")
    public ResponseEntity<Map<String, Object>> assignRoles(@RequestParam("email") String email, @RequestBody AuthoritiesRequestDTO authoritiesRequestDTO) {
        UserCredentials userCredentials = modelMapper.map(authoritiesRequestDTO, UserCredentials.class);
        userCredentialsService.assignRole(authoritiesRequestDTO.getAuthorities(), email);
        return jsonResponseHandler.get("Updated roles for user with email: " + email, HttpStatus.OK.value(), HttpStatus.OK);
    }
}
