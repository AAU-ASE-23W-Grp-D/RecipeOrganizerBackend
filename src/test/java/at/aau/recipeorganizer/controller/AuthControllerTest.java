package at.aau.recipeorganizer.controller;

import at.aau.recipeorganizer.configuration.jwt.JwtUtils;
import at.aau.recipeorganizer.data.SignupRequest;
import at.aau.recipeorganizer.repository.RecipeRepository;
import at.aau.recipeorganizer.repository.RoleRepository;
import at.aau.recipeorganizer.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(AuthController.class)
public class AuthControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeRepository recipeRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
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
