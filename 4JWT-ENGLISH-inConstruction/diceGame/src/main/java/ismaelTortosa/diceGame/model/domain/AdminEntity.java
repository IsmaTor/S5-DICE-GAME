package ismaelTortosa.diceGame.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "admin")
public class AdminEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id_admin;
    @Column(name = "name")
    private String name;
    @Column(name = "password")
    private String password;
    @Column(name = "rol")
    private final String rolAdmin = "admin";

}
