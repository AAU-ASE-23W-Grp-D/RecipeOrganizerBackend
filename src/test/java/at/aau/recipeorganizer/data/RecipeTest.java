package at.aau.recipeorganizer.data;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {
    String imagePath = System.getProperty("user.dir") + "/src/main/resources/images/Pasta.jpg";

    Path path = Paths.get(imagePath);

    @Test
    void testEquals_Equals() throws IOException {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, Files.readAllBytes(path));
        Recipe recipe2 = recipe1;

        assertEquals(recipe1, recipe2);
    }

    @Test
    void testEquals_NotEquals() throws IOException {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, Files.readAllBytes(path));
        Recipe recipe2 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, Files.readAllBytes(path));
        recipe1.id = 1;
        recipe2.id = 2;

        assertNotEquals(recipe1, recipe2);
    }

    @Test
    void testEquals_NotEquals_WrongName() throws IOException {
        Recipe recipe1 = new Recipe("Test Recipe 1", "Test Ingredient", "Test Description", 5, Files.readAllBytes(path));
        Recipe recipe2 = new Recipe("Test Recipe 2", "Test Ingredient", "Test Description", 5, Files.readAllBytes(path));

        assertNotEquals(recipe1, recipe2);
    }

    @Test
    void testEquals_NotEquals_WrongDescription() throws IOException {
        Recipe recipe1 = new Recipe("Test Recipe 1", "Test Ingredient", "Test Description 1", 5, Files.readAllBytes(path));
        Recipe recipe2 = new Recipe("Test Recipe 1", "Test Ingredient", "Test Description 2", 5, Files.readAllBytes(path));

        assertNotEquals(recipe1, recipe2);
    }

    @Test
    void testEquals_NotEquals_NullObject() throws IOException {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, Files.readAllBytes(path));
        Recipe recipe2 = null;

        assertNotEquals(recipe1, recipe2);
    }

    @Test
    void testEquals_NotEquals_WrongObject() throws IOException {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, Files.readAllBytes(path));
        Object recipe2 = new Object();

        assertNotEquals(recipe1, recipe2);
    }

    @Test
    void testHashCode_Equals() throws IOException {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, Files.readAllBytes(path));
        Recipe recipe2 = recipe1;

        assertEquals(recipe1.hashCode(), recipe2.hashCode());
    }

    @Test
    void testHashCode() throws IOException {
        Recipe recipe1 = new Recipe("Test Recipe 1", "Test Ingredient", "Test Description", 5, Files.readAllBytes(path));
        Recipe recipe2 = new Recipe("Test Recipe 2", "Test Ingredient", "Test Description", 5, Files.readAllBytes(path));

        assertNotEquals(recipe1.hashCode(), recipe2.hashCode());
    }

    @Test
    void testHashCode_NameNull() throws IOException {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description 1", 5, Files.readAllBytes(path));
        Recipe recipe2 = new Recipe(null, "Test Ingredient", "Test Description 2", 5, Files.readAllBytes(path));

        assertNotEquals(recipe1.hashCode(), recipe2.hashCode());
    }

    @Test
    void testHashCode_IngredientsNull() throws IOException {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description 1", 5, Files.readAllBytes(path));
        Recipe recipe2 = new Recipe("Test Recipe", null, "Test Description 2", 5, Files.readAllBytes(path));

        assertNotEquals(recipe1.hashCode(), recipe2.hashCode());
    }

    @Test
    void testHashCode_DescriptionNull() throws IOException {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description 1", 5, Files.readAllBytes(path));
        Recipe recipe2 = new Recipe("Test Recipe", "Test Ingredient", null, 5, Files.readAllBytes(path));

        assertNotEquals(recipe1.hashCode(), recipe2.hashCode());
    }

    @Test
    void testToString() throws IOException {
        Recipe recipe = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, Files.readAllBytes(path));
        String expectedToString = "Recipe{id=" + recipe.id + ", name='" + recipe.name + "', ingredients='" + recipe.ingredients + "', description='" + recipe.description + "', rating='" + recipe.rating + "'}";

        assertEquals(expectedToString, recipe.toString());
    }

    @Test
    void testUpdate_NameNull() throws IOException {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, Files.readAllBytes(path));
        Recipe recipe2 = new Recipe(null, "Test Ingredient", "Test Description", 5, Files.readAllBytes(path));
        recipe1.update(recipe2);

        assertNotEquals(recipe1.name, recipe2.name);
    }

    @Test
    void testUpdate_IngredientsNull() throws IOException {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, Files.readAllBytes(path));
        Recipe recipe2 = new Recipe("Test Recipe", null, "Test Description", 5, Files.readAllBytes(path));
        recipe1.update(recipe2);

        assertNotEquals(recipe1.ingredients, recipe2.ingredients);
    }

    @Test
    void testUpdate_DescriptionNull() throws IOException {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, Files.readAllBytes(path));
        Recipe recipe2 = new Recipe("Test Recipe", "Test Ingredient", null, 5, Files.readAllBytes(path));
        recipe1.update(recipe2);

        assertNotEquals(recipe1.description, recipe2.description);
    }

    @Test
    void testGetLikedByUser() throws IOException {
        User user1 = new User("testUser1", "test1@email.com", "testPassword");
        User user2 = new User("testUser2", "test2@email.com", "testPassword");

        Recipe recipe1 = new Recipe("Test Recipe 1", "Test Ingredient", "Test Description", 5, Files.readAllBytes(path));

        user1.addLikedRecipe(recipe1);
        user2.addLikedRecipe(recipe1);

        assertEquals(2, recipe1.getLikedByUser().size());
    }

    private byte[] loadImageFromFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return Files.readAllBytes(path);
        } catch (Exception e) {
            // error message
            return new byte[0];
        }
    }
}
