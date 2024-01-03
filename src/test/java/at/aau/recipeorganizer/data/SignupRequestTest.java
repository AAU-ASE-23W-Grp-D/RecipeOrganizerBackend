package at.aau.recipeorganizer.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SignupRequestTest {
    @Test
    void testSignupRequest() {
        SignupRequest request = new SignupRequest("testUser", "test@email.com", "testPassword");

        assertEquals("testUser", request.username());
    }
}
