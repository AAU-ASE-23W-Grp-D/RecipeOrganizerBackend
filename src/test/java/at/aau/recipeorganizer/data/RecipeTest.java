package at.aau.recipeorganizer.data;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class RecipeTest {
    private final byte[] image = new byte[]{0x01};

    @Test
    void testEquals_Equals() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, 1, image);
        Recipe recipe2 = recipe1;

        assertEquals(recipe1, recipe2);
    }

    @Test
    void testEquals_NotEquals() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, 1, image);
        Recipe recipe2 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, 1, image);
        recipe1.id = 1;
        recipe2.id = 2;

        assertNotEquals(recipe1, recipe2);
    }

    @Test
    void testEquals_NotEquals_WrongName() {
        Recipe recipe1 = new Recipe("Test Recipe 1", "Test Ingredient", "Test Description", 5, 1, image);
        Recipe recipe2 = new Recipe("Test Recipe 2", "Test Ingredient", "Test Description", 5, 1, image);

        assertNotEquals(recipe1, recipe2);
    }

    @Test
    void testEquals_NotEquals_WrongDescription() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description 1", 5, 1, image);
        Recipe recipe2 = new Recipe("Test Recipe", "Test Ingredient", "Test Description 2", 5, 1, image);

        assertNotEquals(recipe1, recipe2);
    }

    @Test
    void testEquals_NotEquals_WrongRating() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 1, 1, image);
        Recipe recipe2 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 2, 1, image);

        assertNotEquals(recipe1, recipe2);
    }

    @Test
    void testEquals_NotEquals_WrongRatingAmount() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 2, 1, image);
        Recipe recipe2 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 2, 2, image);

        assertNotEquals(recipe1, recipe2);
    }

    @Test
    void testEquals_NotEquals_WrongImage() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 1, 1, image);
        Recipe recipe2 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 1, 1, new byte[]{0x02});

        assertNotEquals(recipe1, recipe2);
    }

    @Test
    void testEquals_NotEquals_NullObject() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, 1, image);
        Recipe recipe2 = null;

        assertNotEquals(recipe1, recipe2);
    }

    @Test
    void testEquals_NotEquals_WrongObject() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, 1, image);
        Object recipe2 = new Object();

        assertNotEquals(recipe1, recipe2);
    }

    @Test
    void testHashCode_Equals() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, 1, image);
        Recipe recipe2 = recipe1;

        assertEquals(recipe1.hashCode(), recipe2.hashCode());
    }

    @Test
    void testHashCode() {
        Recipe recipe1 = new Recipe("Test Recipe 1", "Test Ingredient", "Test Description", 5, 1, image);
        Recipe recipe2 = new Recipe("Test Recipe 2", "Test Ingredient", "Test Description", 5, 1, image);

        assertNotEquals(recipe1.hashCode(), recipe2.hashCode());
    }

    @Test
    void testHashCode_NameNull() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description 1", 5, 1, image);
        Recipe recipe2 = new Recipe(null, "Test Ingredient", "Test Description 2", 5, 1, image);

        assertNotEquals(recipe1.hashCode(), recipe2.hashCode());
    }

    @Test
    void testHashCode_IngredientsNull() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description 1", 5, 1, image);
        Recipe recipe2 = new Recipe("Test Recipe", null, "Test Description 2", 5, 1, image);

        assertNotEquals(recipe1.hashCode(), recipe2.hashCode());
    }

    @Test
    void testHashCode_DescriptionNull() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description 1", 5, 1, image);
        Recipe recipe2 = new Recipe("Test Recipe", "Test Ingredient", null, 5, 1, image);

        assertNotEquals(recipe1.hashCode(), recipe2.hashCode());
    }

    @Test
    void testHashCode_RatingNull() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, 1, image);
        Recipe recipe2 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 0, 1, image);

        assertNotEquals(recipe1.hashCode(), recipe2.hashCode());
    }

    @Test
    void testHashCode_RatingAmountNull() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, 1, image);
        Recipe recipe2 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 1, 0, image);

        assertNotEquals(recipe1.hashCode(), recipe2.hashCode());
    }

    @Test
    void testToString() {
        Recipe recipe = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, 1, image);
        String expectedToString = "Recipe{id=" + recipe.id + ", name='" + recipe.name + "', ingredients='" + recipe.ingredients + "', description='" + recipe.description + "', rating='" + recipe.rating + "', rating_amount='" + recipe.rating_amount + "'}";

        assertEquals(expectedToString, recipe.toString());
    }

    @Test
    void testUpdate_NameNull() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, 1, image);
        Recipe recipe2 = new Recipe(null, "Test Ingredient", "Test Description", 5, 1, image);
        recipe1.update(recipe2);

        assertNotEquals(recipe1.name, recipe2.name);
    }

    @Test
    void testUpdate_IngredientsNull() throws IOException {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, 1, image);
        Recipe recipe2 = new Recipe("Test Recipe", null, "Test Description", 5, 1, image);
        recipe1.update(recipe2);

        assertNotEquals(recipe1.ingredients, recipe2.ingredients);
    }

    @Test
    void testUpdate_DescriptionNull() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, 1, image);
        Recipe recipe2 = new Recipe("Test Recipe", "Test Ingredient", null, 5, 1, image);
        recipe1.update(recipe2);

        assertNotEquals(recipe1.description, recipe2.description);
    }

    @Test
    void testUpdate_RatingNotZero() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, 1, image);
        Recipe recipe2 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 0, 1, image);
        recipe1.update(recipe2);

        assertEquals(recipe1.rating, recipe2.rating);
    }

    @Test
    void testUpdate_RatingAmountNotZero() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, 1, image);
        Recipe recipe2 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, 0, image);
        recipe1.update(recipe2);

        assertEquals(recipe1.rating_amount, recipe2.rating_amount);
    }

    @Test
    void testUpdate_ImageNull() {
        Recipe recipe1 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, 1, image);
        Recipe recipe2 = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, 1, null);
        recipe1.update(recipe2);

        assertNotEquals(recipe1.image, recipe2.image);
    }

    @Test
    void testGetLikedByUser() {
        User user1 = new User("testUser1", "test1@email.com", "testPassword");
        User user2 = new User("testUser2", "test2@email.com", "testPassword");

        Recipe recipe1 = new Recipe("Test Recipe 1", "Test Ingredient", "Test Description", 5, 1, image);

        user1.addLikedRecipe(recipe1);
        user2.addLikedRecipe(recipe1);

        assertEquals(2, recipe1.getLikedByUser().size());
    }

    @Test
    void testDeleteOwnRecipe() {
        Recipe recipe = new Recipe("Test Recipe", "Test Ingredient", "Test Description", 5, 1, image);
        User user2 = new User("testUser2", "test2@email.com", "testPassword");

        user2.addOwnRecipe(recipe);

        assertEquals(1, user2.getOwnRecipes().size());

        user2.removeOwnRecipe(recipe);

        assertEquals(0, user2.getOwnRecipes().size());
    }
}
