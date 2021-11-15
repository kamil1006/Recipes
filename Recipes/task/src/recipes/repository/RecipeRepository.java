package recipes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import recipes.entity.Recipe;
import recipes.entity.RecipeTrimmed;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    Recipe findRecipeByName(String name);
    Recipe findById(int id);
    List<Recipe> findAllByCategoryIgnoreCaseOrderByDateDesc(String category);
    List<Recipe> findAllByNameContainingIgnoreCaseOrderByDateDesc(String name);
    Recipe findRecipeByNameAndDate(String name, LocalDateTime date);
    Recipe findRecipeByDate(LocalDateTime date);

   // List<RecipeTrimmed> findAllByCategoryIgnoreCaseOrderByDateAsc(String category);
   // List<RecipeTrimmed> findAllByNameContainingIgnoreCaseOrderByDateAsc(String name);

}
