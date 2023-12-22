package at.aau.recipeorganizer.service;

import at.aau.recipeorganizer.data.User;
import at.aau.recipeorganizer.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserDetailsServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void testLoadUserByUsername_Success() {
        String username = "testUser";
        User user = new User(username, "test@email.com", "testPassword");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertEquals(username, userDetails.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_Failed() {
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
    }
}
