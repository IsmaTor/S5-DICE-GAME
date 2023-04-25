package ismaelTortosa.diceGame.controllers;

import ismaelTortosa.diceGame.model.dto.AdminDTO;
import ismaelTortosa.diceGame.model.exceptions.DuplicateNameException;
import ismaelTortosa.diceGame.model.exceptions.ErrorResponseMessage;
import ismaelTortosa.diceGame.model.repository.AdminRepository;
import ismaelTortosa.diceGame.model.services.IAdminServicesDAO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@Controller
@RequestMapping("/admins")
public class AdminController {
    private static final Logger LOGGER = Logger.getLogger((AdminController.class.getName()));
    private ErrorResponseMessage errorResponse;

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private IAdminServicesDAO adminServices;

    @PostMapping(path= "/add") //http://localhost:9001/admins/add
    public ResponseEntity<?> addUser(@RequestBody AdminDTO adminDTO){
        try {
            if (adminRepository.existsByName(adminDTO.getName())) {
                errorResponse = new ErrorResponseMessage(HttpStatus.NOT_MODIFIED.value(), "User not created.", "User with this name already exists.");
                LOGGER.warning("User not created. User with name " + adminDTO.getName() + " already exists.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_MODIFIED);
            } else {
                adminServices.add(adminDTO);
                LOGGER.info("User " + adminDTO.getName() + " registered successfully.");
                return new ResponseEntity<>(adminDTO, HttpStatus.CREATED);
            }
        } catch (DuplicateNameException e) {
            errorResponse = new ErrorResponseMessage(HttpStatus.NOT_MODIFIED.value(), "User not created.", "User with this name already exists.");
            LOGGER.warning("User not created. " + e);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            errorResponse = new ErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "User not created.", "Failed to create user in database.");
            LOGGER.warning("User not created. " + e);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path= "/update/{id}") //http://localhost:9001/admins/update/?
    public ResponseEntity<?> updateUser(@PathVariable("id") Integer id, @RequestBody AdminDTO adminDTO, HttpServletRequest request){
        boolean validatedAdmin, validatedId;
        AdminDTO adminUpdated;

        String token = request.getHeader("Authorization");

        try{
            validatedAdmin = adminServices.validateAdminAccess(id, token, request);
            validatedId = adminServices.validationTokenId(id, request);

            if(validatedAdmin && validatedId){
                if(adminRepository.existsByName(adminDTO.getName())){
                    errorResponse = new ErrorResponseMessage(HttpStatus.NOT_MODIFIED.value(), "User not created.", "User with this name already exists.");
                    LOGGER.warning("User not created. User with name " + adminDTO.getName() + " already exists.");
                    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_MODIFIED);
                } else {
                    adminUpdated = adminServices.update(id, adminDTO);
                    LOGGER.info("User " + adminDTO.getName() + " it has been updated.");
                    return new ResponseEntity<>(adminUpdated, HttpStatus.OK);
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

}
