package at.aau.recipeorganizer.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long id;

    @Column(name = "name")
    public String name;

    @Column(name = "ingredients")
    public String ingredients;

    @Column(name = "description")
    public String description;

    public Recipe(String name, String ingredients, String description) {
        this.name = name;
        this.ingredients = ingredients;
        this.description = description;
    }

    public Recipe() {
    }

    public Recipe update(Recipe recipe) {
        if (recipe.name != null) this.name = recipe.name;
        if (recipe.ingredients != null) this.ingredients = recipe.ingredients;
        if (recipe.description != null) this.description = recipe.description;

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;

        if (recipe_id != recipe.recipe_id) return false;
        if (!Objects.equals(name, recipe.name)) return false;
        return Objects.equals(description, recipe.description);
    }

    @Override
    public int hashCode() {
        int result = (int) (recipe_id ^ (recipe_id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (ingredients != null ? ingredients.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        // add rating
        return "Recipe{" + "id=" + recipe_id + ", name='" + name + '\'' + ", ingredients='" + ingredients + '\'' + ", description='" + description + '\'' + '}';
    }
}
