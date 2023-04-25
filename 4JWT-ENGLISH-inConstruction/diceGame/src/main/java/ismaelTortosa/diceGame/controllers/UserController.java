package ismaelTortosa.diceGame.controllers;

import ismaelTortosa.diceGame.model.dto.UserDTO;
import ismaelTortosa.diceGame.model.exceptions.DuplicateNameException;
import ismaelTortosa.diceGame.model.exceptions.ErrorResponseMessage;
import ismaelTortosa.diceGame.model.repository.UserRepository;
import ismaelTortosa.diceGame.model.services.IAdminServicesDAO;
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
public class UserController {
    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());
    private ErrorResponseMessage errorResponse;

    //variables
    private List<UserDTO> usersList;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IUserServicesDAO userServices;
    @Autowired
    private IAdminServicesDAO adminServices;

    @PostMapping(path= "/add") //http://localhost:9001/players/add
    public ResponseEntity<?> addUser(@RequestBody UserDTO userDTO){
        try {
            if (userRepository.existsByName(userDTO.getName())) {
                errorResponse = new ErrorResponseMessage(HttpStatus.BAD_REQUEST.value(), "User not created.", "User with this name already exists.");
                LOGGER.warning("User not created. User with name " + userDTO.getName() + " already exists.");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            } else {
                userServices.add(userDTO);
                LOGGER.info("User " + userDTO.getName() + " registered successfully.");
                return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
            }
        } catch (DuplicateNameException e) {
            errorResponse = new ErrorResponseMessage(HttpStatus.BAD_REQUEST.value(), "User not created.", "User with this name already exists.");
            LOGGER.warning("User not created. " + e);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            errorResponse = new ErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "User not created.", "Failed to create user in database.");
            LOGGER.warning("User not created. " + e);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path= "/update/{id}") //http://localhost:9001/players/update/?
    public ResponseEntity<?> updateUser(@PathVariable("id") Integer id, @RequestBody UserDTO userDTO, HttpServletRequest request){
        boolean validated, validatedRolUser;
        UserDTO userUpdated;

        String token = request.getHeader("Authorization");

        try{
            validated = userServices.validationToken(id, request);
            validatedRolUser = userServices.validateRolUserAccess(id, token, request);

            if(validated && validatedRolUser){
                if(userRepository.existsByName(userDTO.getName())){
                    errorResponse = new ErrorResponseMessage(HttpStatus.NOT_MODIFIED.value(), "User not created.", "User with this name already exists.");
                    LOGGER.warning("User not created. User with name " + userDTO.getName() + " already exists.");
                    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_MODIFIED);
                } else {
                    userUpdated = userServices.update(id, userDTO);
                    LOGGER.info("User " + userDTO.getName() + " it has been updated.");
                    return new ResponseEntity<>(userUpdated, HttpStatus.OK);
                }
            } else {
                errorResponse = new ErrorResponseMessage(HttpStatus.NOT_FOUND.value(), "ERROR ID.", "ERROR: The ID is not found or TOKEN incorrect.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        }catch (DuplicateNameException e) {
            errorResponse = new ErrorResponseMessage(HttpStatus.BAD_REQUEST.value(), "User not modified.", "User with this name already exists.");
            LOGGER.warning("User not modified. " + e);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            LOGGER.warning("ERROR: User update not possible. " + e);
            errorResponse = new ErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "ERROR: User update not possible.", "Fail of the game or data base system.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/getAll") //http://localhost:9001/players/getAll
    public ResponseEntity<?> getAllUsers(HttpServletRequest request) {

        try {
            usersList = userServices.getAll();
            LOGGER.info("User list: " + usersList.toString());
            return new ResponseEntity<>(usersList, HttpStatus.OK);
        }  catch (Exception e) {
            LOGGER.warning("It has not been possible to show the list of game players. " + e);
            errorResponse = new ErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Players not shown.", "It has not been possible to show the list of game players.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path= "/getAllUp") //http://localhost:9001/players/getAllUp
    public ResponseEntity<?> getAllUpUsers(){

        try{
            usersList = userServices.getAllUp();
            LOGGER.info("User list: " + usersList.toString());
            return new ResponseEntity<>(usersList, HttpStatus.OK);
        } catch (Exception e){
            LOGGER.warning("It has not been possible to show the list of game players. " + e);
            errorResponse = new ErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Players not shown.", "It has not been possible to show the list of game players.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path= "/getAllDown") //http://localhost:9001/players/getAllUp
    public ResponseEntity<?> getAllDownUsers(){

        try{
            usersList = userServices.getAllDown();
            LOGGER.info("User list: " + usersList.toString());
            return new ResponseEntity<>(usersList, HttpStatus.OK);
        } catch (Exception e){
            LOGGER.warning("It has not been possible to show the list of game players. " + e);
            errorResponse = new ErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Players not shown.", "It has not been possible to show the list of game players.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path= "/getUp") //http://localhost:9001/players/getUp
    public ResponseEntity<?> getUpUser(){

        try{
            usersList = userServices.getWinnerUp();
            LOGGER.info("First position: " + usersList);
            return new ResponseEntity<>(usersList, HttpStatus.OK);
        } catch (Exception e){
            LOGGER.warning("It has not been possible to show the first player. " + e);
            errorResponse = new ErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "First player not shown.", "It has not been possible to show the first of game players.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path= "/getDown") //http://localhost:9001/players/getDown
    public ResponseEntity<?> getDownUser(){

        try{
            usersList = userServices.getWinnerDown();
            LOGGER.info("Last position: " + usersList);
            return new ResponseEntity<>(usersList, HttpStatus.OK);
        } catch (Exception e){
            LOGGER.warning("It has not been possible to show the last player. " + e);
            errorResponse = new ErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Last player not shown.", "It has not been possible to show the last of game players.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path= "/getAverage") //http://localhost:9001/players/getListDown
    public ResponseEntity<?> getAverage(){
        Double average;

        try{
            average = userServices.averageRanking();
            LOGGER.info("Average of all players: " + average + " %.");
            if(average < 10){
                return new ResponseEntity<>(average + " % OF GAMES WON. LOW INDEX OF GAMES WON.", HttpStatus.OK);
            } else if (average >= 10 && average <30 ) {
                return new ResponseEntity<>(average + " % OF GAMES WON. MIDDLE INDEX OF GAMES WON.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(average + " % OF GAMES WON. HIGH INDEX OF GAMES WON.", HttpStatus.OK);
            }

        } catch (Exception e){
            LOGGER.warning("Averages not found. " + e);
            errorResponse = new ErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Average not found.", "It has not been possible to show the average of game players.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
