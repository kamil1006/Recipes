package recipes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
//@NoArgsConstructor
@Entity
public class Recipe {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @JsonIgnore
    int id;

    @JsonIgnore
    String userName;


    @Column(nullable = false)
    @NotEmpty
    @Size(min = 1)
    private String category;

    @Column(nullable = false)
    private LocalDateTime date;


    @Column(nullable = false)
    @NotEmpty
    @Size(min = 1)
    private String name;

    @Column(nullable = false)
    @NotEmpty
    @Size(min = 1)
    String description;

    @Column(nullable = false)
    @ElementCollection
    @NotEmpty
    @Size(min = 1)
    List<String> ingredients;

    @Column(nullable = false)
    @ElementCollection
    @NotEmpty
    @Size(min = 1)
    List<String> directions;

    public Recipe() {
        name = "";
        description = "";
        ingredients = new ArrayList<>();
        directions = new ArrayList<>();
    }
    public void addIngredient(String in){
        ingredients.add(in);
    }
    public  void addDirections(String dir){
        directions.add(dir);

    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = null;
        this.ingredients = ingredients;
    }

    public void setDirections(List<String> directions) {
        this.directions = null;
        this.directions = directions;
    }
}
