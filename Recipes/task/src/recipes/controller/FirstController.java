package recipes.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import recipes.entity.Recipe;
import recipes.entity.RecipeTrimmed;
import recipes.entity.User;
import recipes.repository.RecipeRepository;
import recipes.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
public class FirstController {

    Recipe recipe;
    Map<Integer,Recipe> recipes;
    int counter;

    //@Autowired
    RecipeRepository recipeRepository;

    //@Autowired
    UserRepository userRepository;
    //@Autowired
    PasswordEncoder encoder;

    @Autowired
    public FirstController(RecipeRepository recipeRepository, UserRepository userRepository, PasswordEncoder encoder) {
        recipe = new Recipe();
        recipes = new HashMap<>();
        counter = 0;

        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @PostMapping(path="/api/recipe/new",  consumes = "application/json" )
    public Object insertRecipe(@RequestBody JsonNode inf, Authentication authentication) {

        if(inf == null || inf.get("name") == null
                || inf.get("ingredients") == null
                || inf.get("description") == null
                || inf.get("directions") == null
                || inf.get("category") == null


        ){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        String name = inf.get("name").asText();
        String description = inf.get("description").asText();
        String category = inf.get("category").asText();


        JsonNode ingr = inf.get("ingredients");
        List<String> ingredients = new ArrayList<>();
        if(ingr.isArray()){
            ArrayNode node = (ArrayNode) ingr;
            for (int i = 0 ; i < node.size(); i++){
                ingredients.add(node.get(i).asText());
            }
        }

        JsonNode direc = inf.get("directions");
        List<String> directions = new ArrayList<>();
        if(direc.isArray()){
            ArrayNode node = (ArrayNode) direc;
            for (int i = 0 ; i < node.size(); i++){
                directions.add(node.get(i).asText());
            }
        }

        recipe = new Recipe();
        recipe.setName(name);
        recipe.setDescription(description);
        recipe.setIngredients(ingredients);
        recipe.setDirections(directions);
        recipe.setCategory(category);

        LocalDateTime date = LocalDateTime.now();
        recipe.setDate(date);

        counter++;
        recipes.put(counter,recipe);


        String name2 = name.replace(" ", "");
        String description2 = description.replace(" ", "");
        String category2 = category.replace(" ", "");



        if(directions.size() == 0
                || ingredients.size() == 0
                || name2.length() == 0
                || description2.length() == 0
                || category2.length() == 0

        ){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }else {
            recipe.setUserName(authentication.getName());
            recipeRepository.save(recipe);
        }


        //int idReturned = recipeRepository.findRecipeByName(recipe.getName()).getId();
        //int idReturned = recipeRepository.findRecipeByNameAndDate(recipe.getName(),recipe.getDate()).getId();
        int idReturned = recipeRepository.findRecipeByDate(recipe.getDate()).getId();

        Gson gson = new Gson();
        Map<String,Integer> mapkaOdpowiedzi = new HashMap<>();
        //mapkaOdpowiedzi.put("id",counter);
        mapkaOdpowiedzi.put("id",idReturned);
        String response = gson.toJson(mapkaOdpowiedzi);

        return response;
    }

    @GetMapping("/api/recipe/{id}")
    public Object getQuizybyId(@PathVariable("id") int id) {

       // Recipe foundValue = recipes.getOrDefault(id, null);

        Recipe foundValue = recipeRepository.findById(id);

        if(foundValue == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        }else return foundValue;

    }


    @DeleteMapping("/api/recipe/{id}")
    public Object deleteQuizybyId(@PathVariable("id") int id, Authentication authentication) {

        // Recipe foundValue = recipes.getOrDefault(id, null);

        Recipe foundValue = recipeRepository.findById(id);


        if(foundValue == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        }else{

            String username = authentication.getName();
            if(foundValue.getUserName() == username){
                recipeRepository.deleteById(id);
                return new ResponseEntity(HttpStatus.NO_CONTENT);

            }else {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }



            //return foundValue;

        }

    }

    @PutMapping(path="/api/recipe/{id}", consumes = "application/json" )
    public Object updateQuizybyId(@PathVariable("id") int id, @RequestBody JsonNode inf,  Authentication authentication) {

        // Recipe foundValue = recipes.getOrDefault(id, null);

        Recipe foundValue = recipeRepository.findById(id);


        if(foundValue == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        }else{

            String username = authentication.getName();
            if(!foundValue.getUserName().equals(username)){
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }else

            if(veryfiInputData(inf)) {
                String name = inf.get("name").asText();
                String description = inf.get("description").asText();
                String category = inf.get("category").asText();


                JsonNode ingr = inf.get("ingredients");
                List<String> ingredients = new ArrayList<>();
                if(ingr.isArray()){
                    ArrayNode node = (ArrayNode) ingr;
                    for (int i = 0 ; i < node.size(); i++){
                        ingredients.add(node.get(i).asText());
                    }
                }

                JsonNode direc = inf.get("directions");
                List<String> directions = new ArrayList<>();
                if(direc.isArray()){
                    ArrayNode node = (ArrayNode) direc;
                    for (int i = 0 ; i < node.size(); i++){
                        directions.add(node.get(i).asText());
                    }
                }
                LocalDateTime date = LocalDateTime.now();
                foundValue.setDate(date);
                foundValue.setDescription(description);
                foundValue.setDirections(directions);
                foundValue.setIngredients(ingredients);
                foundValue.setName(name);
                foundValue.setCategory(category);
                recipeRepository.save(foundValue);



                //recipeRepository.deleteById(id);
                //return foundValue;
                return new ResponseEntity(HttpStatus.NO_CONTENT);



            }
            else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }

        }

    }

    public boolean veryfiInputData(JsonNode inf){

        if(inf == null || inf.get("name") == null
                || inf.get("ingredients") == null
                || inf.get("description") == null
                || inf.get("directions") == null
                || inf.get("category") == null


        ){
            return false;
        }

        String name = inf.get("name").asText();
        String description = inf.get("description").asText();
        String category = inf.get("category").asText();
        String name2 = name.replace(" ", "");
        String description2 = description.replace(" ", "");
        String category2 = category.replace(" ", "");
        JsonNode ingr = inf.get("ingredients");
        List<String> ingredients = new ArrayList<>();
        if(ingr.isArray()){
            ArrayNode node = (ArrayNode) ingr;
            for (int i = 0 ; i < node.size(); i++){
                ingredients.add(node.get(i).asText());
            }
        }

        JsonNode direc = inf.get("directions");
        List<String> directions = new ArrayList<>();
        if(direc.isArray()){
            ArrayNode node = (ArrayNode) direc;
            for (int i = 0 ; i < node.size(); i++){
                directions.add(node.get(i).asText());
            }
        }

        recipe = new Recipe();
        recipe.setName(name);
        recipe.setDescription(description);
        recipe.setIngredients(ingredients);
        recipe.setDirections(directions);
        recipe.setCategory(category);

        LocalDateTime date = LocalDateTime.now();
        recipe.setDate(date);

        if(directions.size() == 0
                || ingredients.size() == 0
                || name2.length() == 0
                || description2.length() == 0
                || category2.length() == 0

        ){
           return false;
        }else {
            return true;
        }


        //return true;
    }

    @GetMapping("/api/recipe/search")
    public ResponseEntity<String> getRecipesByCategoryOrName(@RequestParam  Map<String,String> allParams) {


        List<Recipe> result;
        //List<RecipeTrimmed> result;

        if(allParams.size() == 0 || allParams.size() > 1 || allParams.equals(null)){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }else {
            String key = "";
            String value = "";
            for( Map.Entry<String, String> wpis :allParams.entrySet()){
                key = wpis.getKey();
                value = wpis.getValue();
            }
            String key2 = key.replaceAll(" ","");
            String value2 = value.replaceAll(" ", "");
            if( (key2.length() == 0 || value2.length() == 0) && (key != "category" || key != "name") ){
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }else {

                switch (key){
                    case "name":
                        //System.out.println(" --------------------> name");
                        result = recipeRepository.findAllByNameContainingIgnoreCaseOrderByDateDesc(value);
                        //return new ResponseEntity(HttpStatus.ACCEPTED);

                        break;
                    case "category":
                        //System.out.println(" -------------------->  category");
                        //return new ResponseEntity(HttpStatus.ACCEPTED);
                       result = recipeRepository.findAllByCategoryIgnoreCaseOrderByDateDesc(value);
                        break;
                    default:
                        return new ResponseEntity(HttpStatus.BAD_REQUEST);
                      //  break;

                }

                Gson gson = new Gson();
                String s;

                List<RecipeTrimmed> collect = result.stream().map(x -> new RecipeTrimmed(x.getCategory(), x.getDate().toString(), x.getName(), x.getDescription(), x.getIngredients(), x.getDirections())).collect(Collectors.toList());
                // resultTrimmed = result;
                s = gson.toJson(collect);

                //s = gson.toJson(result);
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.set("Content-Type", "application/json");
                return ResponseEntity.ok()
                        .headers(responseHeaders)
                        .body(s);

            }

        }


       // return null;
    }
    //-----------------------------------------------------------------------
    private boolean validateEmail(String email){
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";
        EMAIL_PATTERN="^(.+)@(.+)$";
        EMAIL_PATTERN= "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    //-----------------------------------------------------------------------
    private boolean validatePassword(String pas){
        if(pas.length()>7){
            String s = pas.replaceAll(" ", "");
            if(s.length()>7)
                return true;
            else
                return false;
        }

        else return false;
    }
    //-----------------------------------------------------------------------

    @PostMapping(path="/api/register",  consumes = "application/json" )
    public Object registerUser(@RequestBody JsonNode inf) {

        User user= new User();
        String emalia = inf.get("email").asText();
        String password = inf.get("password").asText();
        if(validateEmail(emalia)&&validatePassword(password)){
            //****************************************************
            // check the database if user already exists
            /*
            System.out.println("****************************************************");
            System.out.println(emalia);
            System.out.println(password);
            System.out.println("****************************************************");

             */
            User existing = userRepository.findUserByUsername(emalia);
            boolean jest=false;
            if(existing != null)
                jest = true;



            //****************************************************
                if(!jest){
                    user.setUsername(emalia);
                    //user.setPassword(password);
                    user.setPassword(encoder.encode(password));
                    userRepository.save(user);

                    int k = 0;
                    //System.out.println(userRepository.findAll());
                    HttpHeaders responseHeaders = new HttpHeaders();
                    responseHeaders.set("Content-Type", "application/json");
                    return ResponseEntity.ok()
                            .headers(responseHeaders)
                            .body("");

                }else {
                    //zwroc blad bo jest juz emalia
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);


                }


            }
        else{
            // zwroc blad bo nieprawidlowa walidacja
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }


       // return null;
    }





/*
            *********************
                I Stage
            *********************

    @GetMapping(path = "/api/recipe")
    public Recipe getRecipe(){
       // recipe.setName("Pierwsze");
    return  recipe;
    }

    @PostMapping(path="/api/recipe",  consumes = "application/json" )
    public Recipe insertRecipe(@RequestBody com.fasterxml.jackson.databind.JsonNode inf) {

        String name = inf.get("name").asText();
        String description = inf.get("description").asText();
        String ingredients = inf.get("ingredients").asText();
        String directions = inf.get("directions").asText();

        recipe.setName(name);
        recipe.setDescription(description);
        recipe.setIngredients(ingredients);
        recipe.setDirections(directions);
        return recipe;
}
*/

}
