package ismaelTortosa.diceGame.controllers;

import ismaelTortosa.diceGame.model.domain.PlayEntity;
import ismaelTortosa.diceGame.model.exceptions.ErrorResponseMessage;
import ismaelTortosa.diceGame.model.services.IPlayServicesDAO;
import ismaelTortosa.diceGame.model.services.IUserServicesDAO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.logging.Logger;

@Controller
@RequestMapping("/players")
public class PlayController {

    private static final Logger LOGGER = Logger.getLogger(PlayController.class.getName());
    private ErrorResponseMessage errorResponse;

    @Autowired
    private IPlayServicesDAO playServices;
    @Autowired
    private IUserServicesDAO userServices;

    @PostMapping(path= "/game/{id}") //http://localhost:9001/players/game/?
    public ResponseEntity<?> diceGame(@PathVariable("id") int id, HttpServletRequest request){
        boolean validated;
        PlayEntity throwDice;

        try{
            validated = userServices.validationToken(id, request); //If there is an error in the validation method it will jump 500 (INTERNAL SERVER ERROR).

            if(validated){
                throwDice = playServices.getPlay(id);
                LOGGER.info("Dice 1: " + throwDice.getDice1() + "+ Dice 2: " + throwDice.getDice2()
                        + " = " + throwDice.playResult());
                return new ResponseEntity<>(throwDice, HttpStatus.OK);
            } else {
                errorResponse = new ErrorResponseMessage(HttpStatus.NOT_FOUND.value(), "ID Failed.", "ID indicated is incorrect.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            LOGGER.warning("ERROR: Could not play the game." + e);
            errorResponse = new ErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "System error.", "Could not play the game.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
