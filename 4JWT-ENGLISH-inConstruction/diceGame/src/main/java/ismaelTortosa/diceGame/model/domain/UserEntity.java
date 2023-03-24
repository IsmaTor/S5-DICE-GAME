package ismaelTortosa.diceGame.model.domain;

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
@Table(name = "user")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    //attributes
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id_user;
    @Column(name = "name")
    private String name;
    @Column(name = "date")
    private LocalDate register;
    @Column(name = "dateUpdate")
    private LocalDate registerUpdate;
    private String password;
    private double winner;

    public UserEntity (int id_user, String name, LocalDate register, LocalDate registerUpdate, String password){
        this.id_user = id_user;
        this.name = name;
        this.register = register;
        this.registerUpdate = registerUpdate;
        this.password = password;
    }

}
