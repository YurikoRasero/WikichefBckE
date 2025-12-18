package com.yuri.WikichefBckE.controller;

import com.yuri.WikichefBckE.Service.AuthenticationService;
import com.yuri.WikichefBckE.dao.request.SignUpRequest;
import com.yuri.WikichefBckE.dao.request.SignInRequest;
import com.yuri.WikichefBckE.dao.response.JwtAuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins="http://localhost:4200")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }

}
