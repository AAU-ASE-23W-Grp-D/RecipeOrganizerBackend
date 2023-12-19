package at.aau.recipeorganizer.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleTest {
    @Test
    void testSetId() {
        Role role = new Role();
        role.setId(1);

        assertEquals(1, role.getId());
    }
}
