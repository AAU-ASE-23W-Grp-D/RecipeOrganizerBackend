package at.aau.recipeorganizer.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.io.Serializable;
import java.util.Arrays;
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

    @Column(name = "rating")
    @Min(value = 1, message = "Rating should not be less than 1")
    @Max(value = 5, message = "Rating should not be greater than 5")
    public int rating;

    @Column(name = "rating_amount")
    public int rating_amount;

    @Column(name= "image", columnDefinition="BYTEA")
    public byte[] image;

    @ManyToMany(mappedBy = "likedRecipes")
    private Set<User> likedByUser = new HashSet<>();

    public Set<User> getLikedByUser() {
        return likedByUser;
    }

    public Recipe(String name, String ingredients, String description, int rating, int rating_amount, byte[] image) {
        this.name = name;
        this.ingredients = ingredients;
        this.description = description;
        this.rating = rating;
        this.rating_amount = rating_amount;
        this.image = image;
    }

    public Recipe() {
    }

    public Recipe update(Recipe recipe) {
        if (recipe.name != null) this.name = recipe.name;
        if (recipe.ingredients != null) this.ingredients = recipe.ingredients;
        if (recipe.description != null) this.description = recipe.description;
        this.rating = recipe.rating;
        this.rating_amount = recipe.rating_amount;
        if (recipe.image != null) this.image = recipe.image;

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;

        if (id != recipe.id) return false;
        if (!Objects.equals(name, recipe.name)) return false;
        if (rating !=recipe.rating) return false;
        if (rating_amount != recipe.rating_amount) return false;
        if (!Arrays.equals(image, recipe.image)) return false;
        return Objects.equals(description, recipe.description);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (ingredients != null ? ingredients.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (rating != 0 ? rating : 0);
        result = 31 * result + (rating_amount != 0 ? rating_amount : 0);
        // image?
        return result;
    }

    @Override
    public String toString() {
        // add image?
        return "Recipe{" + "id=" + id + ", name='" + name + '\'' + ", ingredients='" + ingredients + '\'' + ", description='" + description + '\'' + ", rating='" + rating + '\'' + ", rating_amount='" + rating_amount + '\'' + '}';
    }
}
