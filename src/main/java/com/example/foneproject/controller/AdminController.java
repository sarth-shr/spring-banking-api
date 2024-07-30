package com.example.foneproject.controller;

import com.example.foneproject.dto.request.AuthoritiesReqDTO;
import com.example.foneproject.service.UserCredentialsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final UserCredentialsService userCredentialsService;

    @PostMapping("/disable")
    public ResponseEntity<Map<String, Object>> disableUser(@RequestParam("email") String email) {
        return userCredentialsService.disableUser(email);
    }

    @PostMapping("/enable")
    public ResponseEntity<Map<String, Object>> enableUser(@RequestParam("email") String email) {
        return userCredentialsService.enableUser(email);
    }

    @PostMapping("/authorities")
    public ResponseEntity<Map<String, Object>> assignRoles(@RequestParam("email") String email, @RequestBody @Valid AuthoritiesReqDTO authoritiesReqDTO) {
        return userCredentialsService.assignRole(email, authoritiesReqDTO);
    }
}
