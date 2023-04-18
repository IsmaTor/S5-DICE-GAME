package ismaelTortosa.diceGame.model.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "play")
public class PlayEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id_play;
    @Column(name = "dice1")
    private int dice1;
    @Column(name = "dice2")
    private int dice2;
    @Column(name = "result")
    private boolean result;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "id_admin")
    private AdminEntity adminEntity;

    //Constructor to create the plays
    public PlayEntity(UserEntity userEntity){
        this.dice1 = randomNumber();
        this.dice2 = randomNumber();
        this.result = playResult();
        this.userEntity = userEntity;
    }

    //Constructor to create the plays for admin
    public PlayEntity(UserEntity userEntity ,AdminEntity adminEntity){
        this.dice1 = randomNumber();
        this.dice2 = randomNumber();
        this.result = playResult();
        this.userEntity = userEntity;
        this.adminEntity = adminEntity;
    }

    //Create die roll
    public int randomNumber(){
        int ranNum = (int) Math.floor(Math.random() * 6 + 1);
        return ranNum;
    }

    //Result of the play, condition to win
    public boolean playResult(){
        if(this.dice1 + this.dice2 == 7){
            result = true;
        } else {
            result = false;
        }
        return result;
    }
}
