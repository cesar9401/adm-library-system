package com.ayd2.adm.library.system.security;

import com.ayd2.adm.library.system.dto.AuthReqDto;
import com.ayd2.adm.library.system.dto.JwtResDto;
import com.ayd2.adm.library.system.exception.LibException;
import com.ayd2.adm.library.system.security.jwt.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    public JwtResDto createToken(AuthReqDto reqDto) throws LibException {
        var authData = new UsernamePasswordAuthenticationToken(reqDto.username(), reqDto.password());

        try {
            var authentication = authenticationManager.authenticate(authData);
            if (authentication.isAuthenticated()) {
                var userDetails = userDetailsService.loadUserByUsername(reqDto.username());
                var token = jwtService.generateToken(userDetails);
                return new JwtResDto(token);
            }
        } catch (IOException e) {
            log.error("Error:", e);
        }

        throw new LibException("invalid_user");
    }
}
