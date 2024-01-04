package at.aau.recipeorganizer.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {
    @Test
    void testEquals_Equals() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description");
        Recipe recipe2 = recipe1;

        assertEquals(recipe1, recipe2);
    }

    @Test
    void testEquals_NotEquals_WrongName() {
        Recipe recipe1 = new Recipe("Test Recipe 1", "Test Ingredient", "Test Description");
        Recipe recipe2 = new Recipe("Test Recipe 2", "Test Ingredient", "Test Description");

        assertNotEquals(recipe1, recipe2);
    }

    @Test
    void testEquals_NotEquals_WrongDescription() {
        Recipe recipe1 = new Recipe("Test Recipe 1", "Test Ingredient", "Test Description 1");
        Recipe recipe2 = new Recipe("Test Recipe 1", "Test Ingredient", "Test Description 2");

        assertNotEquals(recipe1, recipe2);
    }

    @Test
    void testEquals_NotEquals_NullObject() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description");
        Recipe recipe2 = null;

        assertNotEquals(recipe1, recipe2);
    }

    @Test
    void testEquals_NotEquals_WrongObject() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description");
        Object recipe2 = new Object();

        assertNotEquals(recipe1, recipe2);
    }

    @Test
    void testHashCode_Equals() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description");
        Recipe recipe2 = recipe1;

        assertEquals(recipe1.hashCode(), recipe2.hashCode());
    }

    @Test
    void testHashCode() {
        Recipe recipe1 = new Recipe("Test Recipe 1", "Test Ingredient", "Test Description");
        Recipe recipe2 = new Recipe("Test Recipe 2", "Test Ingredient", "Test Description");

        assertNotEquals(recipe1.hashCode(), recipe2.hashCode());
    }

    @Test
    void testHashCode_NameNull() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description 1");
        Recipe recipe2 = new Recipe(null, "Test Ingredient", "Test Description 2");

        assertNotEquals(recipe1.hashCode(), recipe2.hashCode());
    }

    @Test
    void testHashCode_DescriptionNull() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description 1");
        Recipe recipe2 = new Recipe("Test Recipe", "Test Ingredient", null);

        assertNotEquals(recipe1.hashCode(), recipe2.hashCode());
    }

    @Test
    void testToString() {
        Recipe recipe = new Recipe("Test Recipe", "Test Ingredient", "Test Description");
        String expectedToString = "Recipe{id=" + recipe.id + ", name='" + recipe.name + "', ingredients='" + recipe.ingredients + "', description='" + recipe.description + "'}";

        assertEquals(expectedToString, recipe.toString());
    }

    @Test
    void testUpdate_NameNull() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description");
        Recipe recipe2 = new Recipe(null, "Test Ingredient", "Test Description");
        recipe1.update(recipe2);

        assertNotEquals(recipe1.name, recipe2.name);
    }

    @Test
    void testUpdate_DescriptionNull() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description");
        Recipe recipe2 = new Recipe("Test Recipe", "Test Ingredient", null);
        recipe1.update(recipe2);

        assertNotEquals(recipe1.description, recipe2.description);
    }
}
