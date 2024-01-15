package at.aau.recipeorganizer.data;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "recipes")
public class Recipe implements Serializable {
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

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "liked_by_user",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likedByUser = new HashSet<>();

    public Set<User> getLikedByUser() {
        return likedByUser;
    }

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

        if (id != recipe.id) return false;
        if (!Objects.equals(name, recipe.name)) return false;
        return Objects.equals(description, recipe.description);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (ingredients != null ? ingredients.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        // add rating
        return "Recipe{" + "id=" + id + ", name='" + name + '\'' + ", ingredients='" + ingredients + '\'' + ", description='" + description + '\'' + '}';
    }
}
