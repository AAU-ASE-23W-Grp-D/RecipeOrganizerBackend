package at.aau.recipeorganizer.service;

import at.aau.recipeorganizer.data.Role;
import at.aau.recipeorganizer.data.SignupRequest;
import at.aau.recipeorganizer.data.User;
import at.aau.recipeorganizer.repository.RoleRepository;
import at.aau.recipeorganizer.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testRegisterUser_Success() {
        SignupRequest signupRequest = new SignupRequest("testUser", "test@email.com", "testPassword");
        when(userRepository.existsByUsername(signupRequest.username())).thenReturn(false);
        when(userRepository.existsByEmail(signupRequest.email())).thenReturn(false);
        when(roleRepository.findByName(Role.ERole.ROLE_USER)).thenReturn(java.util.Optional.of(new Role(Role.ERole.ROLE_USER)));

        UserService.UserSignUpResult result = userService.registerUser(signupRequest);

        assertEquals(UserService.UserSignUpResult.SUCCESS, result);
        Mockito.verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterUser_UsernameTaken() {
        SignupRequest signupRequest = new SignupRequest("newTestUser", "test@email.com", "testPassword");
        when(userRepository.existsByUsername(signupRequest.username())).thenReturn(true);

        UserService.UserSignUpResult result = userService.registerUser(signupRequest);

        assertEquals(UserService.UserSignUpResult.USERNAME_TAKEN, result);
        Mockito.verify(userRepository, Mockito.never()).save(any(User.class));
    }

    @Test
    void testRegisterUser_EmailTaken() {
        SignupRequest signupRequest = new SignupRequest("newTestUser", "existing@email.com", "testPassword");
        when(userRepository.existsByUsername(signupRequest.username())).thenReturn(false);
        when(userRepository.existsByEmail(signupRequest.email())).thenReturn(true);

        UserService.UserSignUpResult result = userService.registerUser(signupRequest);

        assertEquals(UserService.UserSignUpResult.EMAIL_TAKEN, result);
        Mockito.verify(userRepository, Mockito.never()).save(any(User.class));
    }
}
