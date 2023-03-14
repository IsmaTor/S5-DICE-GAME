package S05T02F02TortosadelaIglesiaIsmael.model.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Document(collection = "usuario")
public class UsuarioEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id_usuario;

    private String nombre = "ANÓNIMO";

    private LocalDate registro;

    private double exito;
/*
    public UsuarioEntity(String id_usuario, String nombre, LocalDate registro) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.registro = registro;
    }*/

}


