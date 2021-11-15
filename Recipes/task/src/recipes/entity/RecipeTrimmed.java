package recipes.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class RecipeTrimmed {

    private String category;
    private String date;
    private String name;
    String description;
    List<String> ingredients;
    List<String> directions;



}
