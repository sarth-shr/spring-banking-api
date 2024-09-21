package com.example.foneproject.controller;

import com.example.foneproject.dto.request.AuthoritiesReqDTO;
import com.example.foneproject.service.UserCredentialsService;
import com.example.foneproject.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final UserCredentialsService userCredentialsService;

    @PutMapping("/disable")
    public ResponseEntity<ApiResponse> disableUser(@RequestParam("email") String email) {
        return userCredentialsService.disableUser(email);
    }

    @PutMapping("/enable")
    public ResponseEntity<ApiResponse> enableUser(@RequestParam("email") String email) {
        return userCredentialsService.enableUser(email);
    }

    @PutMapping("/authorities")
    public ResponseEntity<ApiResponse> assignRoles(@RequestParam("email") String email, @RequestBody @Valid AuthoritiesReqDTO authoritiesReqDTO) {
        return userCredentialsService.assignRole(email, authoritiesReqDTO);
    }
}
