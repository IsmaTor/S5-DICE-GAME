package S05T02F02TortosadelaIglesiaIsmael.model.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Setter
@Getter
@ToString
@NoArgsConstructor

@Document(collection = "jugada")
public class JugadaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id_jugada;

    private int dado1;

    private int dado2;

    private boolean resultado;

    private UsuarioEntity usuarioEntity;

    private String idUsuario;

    public JugadaEntity(UsuarioEntity usuarioEntity){
        this.dado1 = numeroAleatorio();
        this.dado2 = numeroAleatorio();
        this.resultado = resultadoJugada();
        this.usuarioEntity = usuarioEntity;
        this.idUsuario = usuarioEntity.getId_usuario();
    }

    public int numeroAleatorio(){

        int numAle = (int) Math.floor(Math.random() * 6 + 1);
        return numAle;
    }

    public boolean resultadoJugada(){

        if(this.dado1 + this.dado2 == 7){
            resultado = true;
        } else {
            resultado = false;
        }
        return resultado;
    }

}
