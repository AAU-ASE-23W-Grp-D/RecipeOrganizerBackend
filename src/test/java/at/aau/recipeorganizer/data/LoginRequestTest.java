package at.aau.recipeorganizer.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginRequestTest {
    @Test
    void testLoginRequest() {
        LoginRequest request = new LoginRequest("testUser", "testPassword");

        assertEquals("testUser", request.username());
    }
}
