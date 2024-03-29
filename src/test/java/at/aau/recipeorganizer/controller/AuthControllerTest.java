package at.aau.recipeorganizer.controller;

import at.aau.recipeorganizer.configuration.jwt.JwtUtils;
import at.aau.recipeorganizer.data.*;
import at.aau.recipeorganizer.repository.RecipeRepository;
import at.aau.recipeorganizer.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static at.aau.recipeorganizer.configuration.jwt.JwtUtils.JWT_COOKIE;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

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

    private final byte[] image = new byte[]{0x01};

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

    @Test
    void testSignOut() throws Exception {
        mockMvc.perform(post("/api/auth/signout"))
                .andExpect(status().isOk())
                .andExpect(content().string("Sign out successfully!"));
    }

    @Test
    void testGetOwnRecipes_Success() throws Exception {
        User user = new User("testUser", "test@email.com", "testPassword");
        Recipe recipe1 = new Recipe("Test Recipe 1", "Test Ingredient", "Test Description", 5, 1, image);
        Recipe recipe2 = new Recipe("Test Recipe 2", "Test Ingredient", "Test Description", 5, 1, image);
        user.addOwnRecipe(recipe1);
        user.addOwnRecipe(recipe2);

        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("testUser");
        when(userService.getUserFromUserName("testUser")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/auth/ownRecipes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer mockToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[1].id").exists());
    }

    @Test
    void testGetOwnRecipes_Failure() throws Exception {
        User user = new User("testUser", "test@email.com", "testPassword");
        Recipe recipe1 = new Recipe("Test Recipe 1", "Test Ingredient", "Test Description", 5, 1, image);
        Recipe recipe2 = new Recipe("Test Recipe 2", "Test Ingredient", "Test Description", 5, 1, image);
        user.addOwnRecipe(recipe1);
        user.addOwnRecipe(recipe2);

        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("testUser");
        when(userService.getUserFromUserName("testUser")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/auth/ownRecipes")
                        .header(HttpHeaders.AUTHORIZATION, ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPostRecipe_Success() throws Exception {
        User user = new User("testUser", "test@email.com", "testPassword");
        Recipe recipe1 = new Recipe("Test Recipe 1", "Test Ingredient", "Test Description", 5, 1, image);

        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("testUser");
        when(userService.getUserFromUserName("testUser")).thenReturn(Optional.of(user));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe1);

        mockMvc.perform(post("/api/auth/postRecipe")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer mockToken")
                        .content(objectMapper.writeValueAsString(recipe1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test Recipe 1"))
                .andExpect(jsonPath("$.ingredients").value("Test Ingredient"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.rating_amount").value(1));
    }

    @Test
    void testPostRecipe_Failure() throws Exception {
        User user = new User("testUser", "test@email.com", "testPassword");
        Recipe recipe1 = new Recipe("Test Recipe 1", "Test Ingredient", "Test Description", 5, 1, image);

        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("testUser");
        when(userService.getUserFromUserName("testUser")).thenReturn(Optional.of(user));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe1);

        mockMvc.perform(post("/api/auth/postRecipe")
                        .header(HttpHeaders.AUTHORIZATION, "")
                        .content(objectMapper.writeValueAsString(recipe1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetLikedRecipes_Success() throws Exception {
        User user = new User("testUser", "test@email.com", "testPassword");
        Recipe recipe1 = new Recipe("Test Recipe 1", "Test Ingredient", "Test Description", 5, 1, image);
        Recipe recipe2 = new Recipe("Test Recipe 2", "Test Ingredient", "Test Description", 5, 1, image);
        user.addLikedRecipe(recipe1);
        user.addLikedRecipe(recipe2);

        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("testUser");
        when(userService.getUserFromUserName("testUser")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/auth/likedRecipes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer mockToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[1].id").exists());
    }

    @Test
    void testGetLikedRecipes_Failure() throws Exception {
        User user = new User("testUser", "test@email.com", "testPassword");
        Recipe recipe1 = new Recipe("Test Recipe 1", "Test Ingredient", "Test Description", 5, 1, image);
        Recipe recipe2 = new Recipe("Test Recipe 2", "Test Ingredient", "Test Description", 5, 1, image);
        user.addLikedRecipe(recipe1);
        user.addLikedRecipe(recipe2);

        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("testUser");
        when(userService.getUserFromUserName("testUser")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/auth/likedRecipes")
                        .header(HttpHeaders.AUTHORIZATION, ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPostLikedRecipe_Success() throws Exception {
        User user = new User("testUser", "test@email.com", "testPassword");
        Recipe recipe1 = new Recipe("Test Recipe 1", "Test Ingredient", "Test Description", 5, 1, image);

        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("testUser");
        when(userService.getUserFromUserName("testUser")).thenReturn(Optional.of(user));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe1);

        mockMvc.perform(post("/api/auth/postLikedRecipe")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer mockToken")
                        .content(objectMapper.writeValueAsString(recipe1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testPostLikedRecipe_Failure() throws Exception {
        User user = new User("testUser", "test@email.com", "testPassword");
        Recipe recipe1 = new Recipe("Test Recipe 1", "Test Ingredient", "Test Description", 5, 1, image);

        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("testUser");
        when(userService.getUserFromUserName("testUser")).thenReturn(Optional.of(user));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe1);

        mockMvc.perform(post("/api/auth/postLikedRecipe")
                        .header(HttpHeaders.AUTHORIZATION, "")
                        .content(objectMapper.writeValueAsString(recipe1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRemoveLikedRecipe_Success() throws Exception {
        User user = new User("testUser", "test@email.com", "testPassword");
        Recipe recipe1 = new Recipe("Test Recipe 1", "Test Ingredient", "Test Description", 5, 1, image);

        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("testUser");
        when(userService.getUserFromUserName("testUser")).thenReturn(Optional.of(user));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe1);

        mockMvc.perform(post("/api/auth/postLikedRecipe")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer mockToken")
                        .content(objectMapper.writeValueAsString(recipe1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/auth/removeLikedRecipe")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer mockToken")
                        .content(objectMapper.writeValueAsString(recipe1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testRemoveLikedRecipe_Failure() throws Exception {
        User user = new User("testUser", "test@email.com", "testPassword");
        Recipe recipe1 = new Recipe("Test Recipe 1", "Test Ingredient", "Test Description", 5, 1, image);

        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("testUser");
        when(userService.getUserFromUserName("testUser")).thenReturn(Optional.of(user));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe1);

        mockMvc.perform(post("/api/auth/removeLikedRecipe")
                        .header(HttpHeaders.AUTHORIZATION, "")
                        .content(objectMapper.writeValueAsString(recipe1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteRecipe_Success() throws Exception {
        User user = new User("testUser", "test@email.com", "testPassword");
        Recipe recipe1 = new Recipe("Test Recipe 1", "Test Ingredient", "Test Description", 5, 1, image);
        user.addOwnRecipe(recipe1);

        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("testUser");
        when(userService.getUserFromUserName("testUser")).thenReturn(Optional.of(user));
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe1));

        mockMvc.perform(delete("/api/auth/deleteRecipe/{id}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer mockToken"))
                .andExpect(status().isOk())
                .andExpect(content().string("Recipe deleted successfully!"));
    }

    @Test
    void testDeleteRecipe_Failure_NotOwner() throws Exception {
        User user = new User("testUser", "test@email.com", "testPassword");
        Recipe recipe1 = new Recipe("Test Recipe 1", "Test Ingredient", "Test Description", 5, 1, image);

        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("testUser");
        when(userService.getUserFromUserName("testUser")).thenReturn(Optional.of(user));
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe1));

        mockMvc.perform(delete("/api/auth/deleteRecipe/{id}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer mockToken"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unauthorized: You are not the owner of this recipe!"));
    }

    @Test
    void testDeleteRecipe_Failure_UserNotFound() throws Exception {
        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("testUser");
        when(userService.getUserFromUserName("testUser")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/auth/deleteRecipe/{id}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer mockToken"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: User not found!"));
    }
    @Test
    void testDeleteRecipe_Failure_InvalidToken() throws Exception {
        mockMvc.perform(delete("/api/auth/deleteRecipe/{id}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, "Invalid token"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Invalid token!"));
    }

}
