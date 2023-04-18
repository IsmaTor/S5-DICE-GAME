package ismaelTortosa.diceGame.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id_admin;
    private String name;
    private String password;
    private String rol;

}
