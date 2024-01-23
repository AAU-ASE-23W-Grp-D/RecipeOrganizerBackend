package at.aau.recipeorganizer.controller;

import at.aau.recipeorganizer.configuration.jwt.JwtUtils;
import at.aau.recipeorganizer.data.*;
import at.aau.recipeorganizer.service.RecipeService;
import at.aau.recipeorganizer.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final RecipeService service;
    private static final String BEARER = "Bearer ";


    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService, RecipeService recipeService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.service = recipeService;
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

    @PostMapping("/signout")
    public ResponseEntity<String> logoutUser() {
        try {
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok("Sign out successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sign out failure!");
        }
    }

    @PostMapping("/postRecipe")
    public ResponseEntity<Recipe> saveRecipe(@RequestHeader (name = HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody Recipe recipe) {
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(BEARER)) {
            String token = authorizationHeader.substring(7);
            String userName = jwtUtils.getUserNameFromJwtToken(token);
            Optional<User> user = userService.getUserFromUserName(userName);

            if (user.isPresent()) {
                user.get().addOwnRecipe(recipe);
                return ResponseEntity.ok(service.save(recipe));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/postLikedRecipe")
    public ResponseEntity<String> saveLikedRecipe(@RequestHeader (name = HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody Recipe recipe) {
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(BEARER)) {
            String token = authorizationHeader.substring(7);
            String userName = jwtUtils.getUserNameFromJwtToken(token);
            Optional<User> user = userService.getUserFromUserName(userName);

            if (user.isPresent()) {
                user.get().addLikedRecipe(recipe);
                return ResponseEntity.ok("Liked recipe successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/removeLikedRecipe")
    public ResponseEntity<String> removeLikedRecipe(@RequestHeader (name = HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody Recipe recipe) {
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(BEARER)) {
            String token = authorizationHeader.substring(7);
            String userName = jwtUtils.getUserNameFromJwtToken(token);
            Optional<User> user = userService.getUserFromUserName(userName);

            if (user.isPresent()) {
                user.get().removeLikedRecipe(recipe);
                return ResponseEntity.ok("Unliked recipe successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/ownRecipes")
    public ResponseEntity<List<Recipe>> getOwnRecipes(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(BEARER)) {
            String token = authorizationHeader.substring(7);
            String userName = jwtUtils.getUserNameFromJwtToken(token);
            Optional<User> user = userService.getUserFromUserName(userName);

            if (user.isPresent()) {
                List<Recipe> ownRecipes = new ArrayList<>(user.get().getOwnRecipes());
                return ResponseEntity.ok().body(ownRecipes);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/likedRecipes")
    public ResponseEntity<List<Recipe>> getLikedRecipes(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(BEARER)) {
            String token = authorizationHeader.substring(7);
            String userName = jwtUtils.getUserNameFromJwtToken(token);
            Optional<User> user = userService.getUserFromUserName(userName);

            if (user.isPresent()) {
                List<Recipe> likedRecipes = new ArrayList<>(user.get().getLikedRecipes());
                return ResponseEntity.ok().body(likedRecipes);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
