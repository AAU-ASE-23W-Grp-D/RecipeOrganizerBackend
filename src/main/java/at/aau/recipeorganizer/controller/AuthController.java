package at.aau.recipeorganizer.controller;

import at.aau.recipeorganizer.configuration.jwt.JwtUtils;
import at.aau.recipeorganizer.data.LoginRequest;
import at.aau.recipeorganizer.data.SignupRequest;
import at.aau.recipeorganizer.data.User;
import at.aau.recipeorganizer.data.UserInfoResponse;
import at.aau.recipeorganizer.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<UserInfoResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User userDetails = (User) authentication.getPrincipal();
        String jwt = jwtUtils.generateTokenFromUsername(userDetails.getUsername());

        var roles = new ArrayList<String>();
        for (var authority : userDetails.getAuthorities())
            roles.add(authority.getAuthority());

        return ResponseEntity.ok()
                .body(new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), roles, jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return switch (userService.registerUser(signUpRequest)) {
            case USERNAME_TAKEN -> ResponseEntity.badRequest().body("Error: Username is already taken!");
            case EMAIL_TAKEN -> ResponseEntity.badRequest().body("Error: Email is already in use!");
            case SUCCESS -> ResponseEntity.ok("User registered successfully!");
        };
    }
}
