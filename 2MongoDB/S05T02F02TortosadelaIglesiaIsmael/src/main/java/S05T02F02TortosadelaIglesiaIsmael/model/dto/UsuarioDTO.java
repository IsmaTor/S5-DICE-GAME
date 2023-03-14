package S05T02F02TortosadelaIglesiaIsmael.model.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
//@NoArgsConstructor
@Setter
@Getter
@ToString
public class UsuarioDTO {

    private String id_usuario;
    private String nombre = "ANÓNIMO";
    private LocalDate registro;
    private double exito;

    private static int counter = 1;

    public UsuarioDTO(){

        this.nombre = next();
    }

    public static String next(){

        return "ANÓNIMO" + counter++;
    }


}

