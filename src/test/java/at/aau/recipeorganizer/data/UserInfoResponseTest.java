package at.aau.recipeorganizer.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserInfoResponseTest {
    @Test
    void testUserInfoResponse() {
        UserInfoResponse response = new UserInfoResponse(1L, "testUser", null, null);

        assertEquals("testUser", response.username());
    }
}
