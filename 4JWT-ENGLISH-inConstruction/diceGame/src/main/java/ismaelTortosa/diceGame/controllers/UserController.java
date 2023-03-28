package ismaelTortosa.diceGame.controllers;

import ismaelTortosa.diceGame.model.dto.UserDTO;
import ismaelTortosa.diceGame.model.mistakes.DuplicateNameException;
import ismaelTortosa.diceGame.model.mistakes.ErrorResponseMessage;
import ismaelTortosa.diceGame.model.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IUserServicesDAO userServices;

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
        boolean validated;
        UserDTO userUpdated;

        try{
            validated = userServices.validationToken(id, request);

            if(validated){
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
        } catch (Exception e){
            LOGGER.warning("ERROR: User update not possible. " + e);
            errorResponse = new ErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "ERROR: User update not possible.", "Fail of the game or data base system.");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path= "/getAll") //http://localhost:9001/players/getAll
    public ResponseEntity<?> getAllUsers(){
        List<UserDTO> users;

        try{
            users = userServices.getAll();
            LOGGER.info("User list: " + users.toString());
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("There are no players in the system. " + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path= "/getUp") //http://localhost:9001/players/getUp
    public ResponseEntity<?> getUpUser(){
        List<UserDTO> rankingUp;

        try{
            rankingUp = userServices.getWinnerUp();
            LOGGER.info("First position: " + rankingUp);
            return new ResponseEntity<>(rankingUp, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Ranking not found. " + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path= "/getListUp") //http://localhost:9001/players/getListUp
    public ResponseEntity<?> getListUpUsers(){
        List<UserDTO> rankingUpList;

        try{
            rankingUpList = userServices.getAllUp();
            LOGGER.info("List of positions in ascending order. " + rankingUpList);
            return new ResponseEntity<>(rankingUpList, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Ranking not found. " + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path= "/getDown") //http://localhost:9001/players/getDown
    public ResponseEntity<?> getDownUser(){
        List<UserDTO> rankingDown;

        try{
            rankingDown = userServices.getWinnerDown();
            LOGGER.info("Last position: " + rankingDown);
            return new ResponseEntity<>(rankingDown, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Ranking not found. " + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path= "/getListDown") //http://localhost:9001/players/getListDown
    public ResponseEntity<?> getListDownUsers(){
        List<UserDTO> rankingDownList;

        try{
            rankingDownList = userServices.getAllDown();
            LOGGER.info("List of positions in descendent order: " + rankingDownList);
            return new ResponseEntity<>(rankingDownList, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Ranking not found. " + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path= "/getAverage") //http://localhost:9001/players/getListDown
    public ResponseEntity<?> getAverage(){
        Double average;

        try{
            average = userServices.averageRanking();
            LOGGER.info("Average of all players: " + average + " %.");
            return new ResponseEntity<>(average + " %", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Averages not found. " + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
