package ismaelTortosa.diceGame.controllers;

import ismaelTortosa.diceGame.model.dto.UserDTO;
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

    @Autowired
    private IUserServicesDAO userServices;

    @PostMapping(path= "/add") //http://localhost:9001/players/add
    public ResponseEntity<?> addUser(@RequestBody UserDTO userDTO){
        try{
            userServices.add(userDTO);
            LOGGER.info("User " + userDTO.getName() + " registered successfully.");
            return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("There are no players in the system. " + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path= "/update/{id}") //http://localhost:9001/players/update/?
    public ResponseEntity<?> updateUser(@PathVariable("id") Integer id, @RequestBody UserDTO userDTO, HttpServletRequest request){
        boolean validated;
        UserDTO userUpdated;

        try{
            validated = userServices.validationToken(id, request);

            if(validated){
                userUpdated = userServices.update(id, userDTO);
                LOGGER.info("User " + userDTO.getName() + " it has been updated.");
                return new ResponseEntity<>(userUpdated, HttpStatus.OK);
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: The ID is not found.");
            }
        } catch (Exception e){
            LOGGER.info("ERROR: User update not possible. " + e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
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
