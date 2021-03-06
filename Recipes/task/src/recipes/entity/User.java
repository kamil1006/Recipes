package recipes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@Entity
public class User {


    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    //@JsonIgnore
    int id;


    private String username;
    private String password;
    private String role;


    public User() {
        this.role = "ROLE_USER";
    }


}
