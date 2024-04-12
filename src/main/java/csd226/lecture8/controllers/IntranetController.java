package csd226.lecture8.controllers;

import csd226.lecture8.security.JwtTokenUtil;
import csd226.lecture8.security.RefreshTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

public class IntranetController {
    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JwtTokenUtil jwtUtil;

    @Autowired
    RefreshTokenUtil refreshTokenUtil;
}

