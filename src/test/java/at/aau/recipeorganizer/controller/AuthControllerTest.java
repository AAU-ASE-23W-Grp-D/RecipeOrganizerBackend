package at.aau.recipeorganizer.controller;

import at.aau.recipeorganizer.configuration.jwt.JwtUtils;
import at.aau.recipeorganizer.data.LoginRequest;
import at.aau.recipeorganizer.data.Role;
import at.aau.recipeorganizer.data.SignupRequest;
import at.aau.recipeorganizer.data.User;
import at.aau.recipeorganizer.repository.RecipeRepository;
import at.aau.recipeorganizer.repository.RoleRepository;
import at.aau.recipeorganizer.repository.UserRepository;
import at.aau.recipeorganizer.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static at.aau.recipeorganizer.configuration.jwt.JwtUtils.JWT_COOKIE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class AuthControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private RecipeRepository recipeRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private Authentication authentication;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAuthenticateUser() throws Exception {
        User user = new User("testUser", "test@email.com", "testPassword");
        user.roles.add(new Role(Role.ERole.ROLE_USER));
        when(authentication.getPrincipal()).thenReturn(user);
        when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenReturn(authentication);

        ResponseCookie jwtCookie = ResponseCookie.from(JWT_COOKIE, "1234567890").path("/api").maxAge((long) 24 * 60 * 60).httpOnly(true).build();
        when(jwtUtils.generateJwtCookie(Mockito.any(User.class))).thenReturn(jwtCookie);

        LoginRequest request = new LoginRequest("testUser", "testPassword");
        mockMvc.perform(post("/api/auth/signin")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        when(userService.registerUser(any(SignupRequest.class))).thenReturn(UserService.UserSignUpResult.SUCCESS);

        SignupRequest request = new SignupRequest("testUser", "test@email.com", "testPassword");

        mockMvc.perform(post("/api/auth/signup")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string("User registered successfully!"));
    }

    @Test
    void testRegisterUser_UsernameTaken() throws Exception {
        when(userService.registerUser(any(SignupRequest.class))).thenReturn(UserService.UserSignUpResult.USERNAME_TAKEN);

        SignupRequest request = new SignupRequest("testUser", "test@email.com", "testPassword");

        mockMvc.perform(post("/api/auth/signup")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string("Error: Username is already taken!"));
    }

    @Test
    void testRegisterUser_EmailTaken() throws Exception {
        when(userService.registerUser(any(SignupRequest.class))).thenReturn(UserService.UserSignUpResult.EMAIL_TAKEN);

        SignupRequest request = new SignupRequest("testUser", "test@email.com", "testPassword");

        mockMvc.perform(post("/api/auth/signup")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string("Error: Email is already in use!"));
    }
}
