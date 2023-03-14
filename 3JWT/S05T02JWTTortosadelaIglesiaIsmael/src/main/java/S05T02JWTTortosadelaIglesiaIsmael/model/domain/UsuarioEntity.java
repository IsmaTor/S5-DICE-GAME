package S05T02JWTTortosadelaIglesiaIsmael.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class UsuarioEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id_usuario;
    @Column(name = "nombre")
    private String nombre;

    @Column(name = "date")
    private LocalDate registro;

    private String password;

    private double exito;

    public UsuarioEntity(int id_usuario, String nombre, LocalDate registro, String password) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.registro = registro;
        this.password = password;
    }

}

//Se pueden sacar peticiones al repositorio porque ya están las dos tablas relacionadas.