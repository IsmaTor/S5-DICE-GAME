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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/players")
public class PlayController {

    private static final Logger LOGGER = Logger.getLogger(PlayController.class.getName());
    private ErrorResponseMessage errorResponse;

    //variables
    private boolean validatedUser;

    @Autowired
    private IPlayServicesDAO playServices;
    @Autowired
    private IUserServicesDAO userServices;

    @PostMapping(path= "/game/{id}") //http://localhost:9001/players/game/?
    public ResponseEntity<?> diceGame(@PathVariable("id") int id, HttpServletRequest request){
        PlayEntity throwDice;

        String token = request.getHeader("Authorization");

        try{
            validatedUser = userServices.validateUserAccess(id, token, request); //If there is an error in the validation method it will jump 500 (INTERNAL SERVER ERROR).

            if(validatedUser){
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

    @DeleteMapping(path= "/delete/{id}") //http://localhost:9001/players/delete/?
    public ResponseEntity<?> removePlay (@PathVariable("id") int id, HttpServletRequest request){
        StringBuilder sb = new StringBuilder();
        String deletedPlayIdsString;

        String token = request.getHeader("Authorization");

        try {
            validatedUser = userServices.validateUserAccess(id, token, request);

            if (validatedUser) {
                List<Integer> deletedPlayIds = playServices.deletePlay(id);
                LOGGER.info("Plays removed: " + deletedPlayIds);
                for (Integer playId : deletedPlayIds){
                    sb.append(playId.toString()).append(", "); //Add a comma and a space.,
                }
                deletedPlayIdsString = sb.substring(0, sb.length() - 2); //Removes the last comma and the last space.
                return new ResponseEntity<>("Plays removed: " + deletedPlayIdsString, HttpStatus.OK);
            } else {
                errorResponse = new ErrorResponseMessage(HttpStatus.NOT_FOUND.value(), "It was not possible to delete the player's moves", "ID indicated is incorrect" );
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            LOGGER.warning("ERROR: It was not possible to delete the player's moves. They have not been found or have already been deleted." + e);
            errorResponse = new ErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "It was not possible to delete the player's move.", "They have not been found or have already been deleted.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path= "/getOne/{id}") //http://localhost:9001/players/getOne/?
    public ResponseEntity<?> getOnePlayer (@PathVariable("id") int id, HttpServletRequest request){
        try{
            List<PlayEntity> plays = playServices.getOne(id);
            LOGGER.info("Play list: " + plays);
            return new ResponseEntity<>(plays, HttpStatus.OK);
        } catch (Exception e){
            LOGGER.warning("The player's moves could not be displayed." + e);
            errorResponse = new ErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "The player's move could not be displayed.", "They have not been found or have already been deleted.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
