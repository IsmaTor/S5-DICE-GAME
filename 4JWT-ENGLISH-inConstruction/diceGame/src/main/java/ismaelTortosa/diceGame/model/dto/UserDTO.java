package ismaelTortosa.diceGame.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id_user;
    private String name = "Anonymous";
    private LocalDate register;
    private LocalDate registerUpdate;
    private double winner;
    private String password;
    private static int counter = 1;

    //Constructor to create anonymous names
    public UserDTO(){
        this.name = next();
    }

    //Create various anonymous names adding a number
    public static String next(){
        return "Anonymous" + counter++;
    }
}
