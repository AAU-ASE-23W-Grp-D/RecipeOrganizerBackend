package at.aau.recipeorganizer.data;


import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    public ERole name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer id;

    public Role() {
    }

    public Role(ERole name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public enum ERole {
        ROLE_USER,
        ROLE_ADMIN
    }

}
