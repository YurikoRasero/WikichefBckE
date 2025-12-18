package com.yuri.WikichefBckE.Service;

import com.yuri.WikichefBckE.dao.request.SignUpRequest;
import com.yuri.WikichefBckE.dao.request.SignInRequest;
import com.yuri.WikichefBckE.dao.response.JwtAuthenticationResponse;


public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SignInRequest request);

}
