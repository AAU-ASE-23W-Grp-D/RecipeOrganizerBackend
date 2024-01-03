package at.aau.recipeorganizer.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {
    private User user;

    @BeforeEach
    public void init() {
        user = new User("testUser", "test@email.com", "testPassword");
    }

    @Test
    void testUser_GetUserName() {
        assertEquals("testUser", user.getUsername());
    }

    @Test
    void testUser_GetEmail() {
        assertEquals("test@email.com", user.getEmail());
    }

    @Test
    void testUser_GetPassword() {
        assertEquals("testPassword", user.getPassword());
    }

    @Test
    void testGetAuthorities() {
        Role roleUser = new Role(Role.ERole.ROLE_USER);
        Role roleAdmin = new Role(Role.ERole.ROLE_ADMIN);

        user.roles.add(roleUser);
        user.roles.add(roleAdmin);

        assertEquals(2, user.getAuthorities().size());
    }

    @Test
    void testUser_isAccountNonExpired() {
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    void testUser_isAccountNonLocked() {
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void testUser_isCredentialsNonExpired() {
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void testUser_isEnabled() {
        assertTrue(user.isEnabled());
    }

    @Test
    void testSetId() {
        User user = new User();
        user.setId(1L);

        assertEquals(1L, user.getId());
    }
}
