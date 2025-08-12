package com.api.forumHub.controller;

import com.api.forumHub.domain.user.User;
import com.api.forumHub.domain.user.UserAuthenticationData;
import com.api.forumHub.infra.security.ResponseJWTToken;
import com.api.forumHub.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthenticationController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity userLogin(@RequestBody @Valid UserAuthenticationData userAuthenticationData) {
        try {
            var authenticationToken = new UsernamePasswordAuthenticationToken(
                    userAuthenticationData.email(),
                    userAuthenticationData.password()
            );

            var authenticatedUser = authenticationManager.authenticate(authenticationToken);
            String JWTtoken = tokenService.generateToken((User) authenticatedUser.getPrincipal());


            return ResponseEntity.ok(new ResponseJWTToken(JWTtoken));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing the request");
        }
    }
}
