package recipes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import recipes.entity.User;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication

public class RecipesApplication {

    public static List<User> lista = new ArrayList<>();

    public static void main(String[] args) {
        SpringApplication.run(RecipesApplication.class, args);
    }
}
