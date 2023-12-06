package at.aau.recipeorganizer.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "recipes")
public class Recipe {
    @Column(name = "name")
    public String name;

    @Column(name = "description")
    public String description;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    public long id;

    public Recipe(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Recipe() {
    }

    public Recipe update(Recipe recipe) {
        if (recipe.name != null) this.name = recipe.name;
        if (recipe.description != null) this.description = recipe.description;

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;

        if (id != recipe.id) return false;
        if (!Objects.equals(name, recipe.name)) return false;
        return Objects.equals(description, recipe.description);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Recipe{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description + '\'' + '}';
    }
}
