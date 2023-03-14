package S05T02N01TortosadelaIglesiaIsmael.model.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Setter
@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "jugada")
public class JugadaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id_jugada;

    @Column(name = "dado1")
    private int dado1;

    @Column(name = "dado2")
    private int dado2;

    @Column(name = "resultado")
    private boolean resultado;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private UsuarioEntity usuarioEntity;

    //Constructor para crear las jugadas
    public JugadaEntity(UsuarioEntity usuarioEntity){
        this.dado1 = numeroAleatorio();
        this.dado2 = numeroAleatorio();
        this.resultado = resultadoJugada();
        this.usuarioEntity = usuarioEntity;
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
