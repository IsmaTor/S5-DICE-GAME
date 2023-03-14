package S05T02JWTTortosadelaIglesiaIsmael.model.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class UsuarioDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id_usuario;
    private String nombre = "ANÓNIMO";
    private LocalDate registro;
    private double exito;
    private String password;
    private static int counter = 1;

    public UsuarioDTO(){
        this.nombre = next();
    }

    public static String next(){
        return "ANÓNIMO" + counter++;
    }


}