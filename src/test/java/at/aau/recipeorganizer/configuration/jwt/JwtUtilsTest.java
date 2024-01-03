package at.aau.recipeorganizer.configuration.jwt;

import at.aau.recipeorganizer.data.User;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {
    private JwtUtils jwtUtils;

    @BeforeEach
    public void init() {
        jwtUtils = new JwtUtils();

        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 100000000);
    }

    @Test
    void testValidateJwtToken_ValidToken() {
        String jwtToken = jwtUtils.generateTokenFromUsername("testUser");

        assertTrue(jwtUtils.validateJwtToken(jwtToken));
    }

    @Test
    void testValidateJwtToken_InvalidToken() {
        assertFalse(jwtUtils.validateJwtToken("invalidToken"));
    }

    @Test
    void testGetUserNameFromJwtToken() {
        String jwtToken = jwtUtils.generateTokenFromUsername("testUser");
        String username = jwtUtils.getUserNameFromJwtToken(jwtToken);

        assertEquals("testUser", username);
    }

    @Test
    void testGenerateJwtCookie() {
        User user = new User("testUser", "test@email.com", "testPassword");
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);

        assertEquals("JWT_TOKEN", jwtCookie.getName());
    }

    @Test
    void testGetJwtFromHeader() {
        User user = new User("testUser", "test@email.com", "testPassword");
        String jwt = jwtUtils.generateTokenFromUsername(user.getUsername());

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("JWT_TOKEN", jwt);
        Optional<String> jwtFromHeader = jwtUtils.getJwtFromHeader(request);

        assertTrue(jwtFromHeader.isPresent());
    }
}
