package ismaelTortosa.diceGame.controllers;

import ismaelTortosa.diceGame.model.confirmations.Confirmation;
import ismaelTortosa.diceGame.model.dto.AdminDTO;
import ismaelTortosa.diceGame.model.exceptions.DuplicateNameException;
import ismaelTortosa.diceGame.model.exceptions.ErrorResponseMessage;
import ismaelTortosa.diceGame.model.repository.AdminRepository;
import ismaelTortosa.diceGame.model.services.IAdminServices;
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
    //variables
    boolean validatedAdmin;

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private IAdminServices adminServices;

    @PostMapping(path= "/add") //http://localhost:9001/admins/add
    public ResponseEntity<?> addAdmin(@RequestBody AdminDTO adminDTO){
        try {
            if (adminRepository.existsByName(adminDTO.getName())) {
                errorResponse = new ErrorResponseMessage(HttpStatus.NOT_MODIFIED.value(), "Admin not created.", "Admin with this name already exists.");
                LOGGER.warning("Admin not created. Admin with name " + adminDTO.getName() + " already exists.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_MODIFIED);
            } else {
                adminServices.add(adminDTO);
                LOGGER.info("Admin " + adminDTO.getName() + " registered successfully.");
                return new ResponseEntity<>(adminDTO, HttpStatus.CREATED);
            }
        } catch (DuplicateNameException e) {
            errorResponse = new ErrorResponseMessage(HttpStatus.NOT_MODIFIED.value(), "Admin not created.", "Admin with this name already exists.");
            LOGGER.warning("Admin not created. " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            errorResponse = new ErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Admin not created.", "Failed to create admin in database.");
            LOGGER.warning("Admin not created. " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path= "/update/{id}") //http://localhost:9001/admins/update/?
    public ResponseEntity<?> updateAdmin(@PathVariable("id") Integer id, @RequestBody AdminDTO adminDTO, HttpServletRequest request){
        AdminDTO adminUpdated;

        String token = request.getHeader("Authorization");

        try{
            validatedAdmin = adminServices.validateAdminAccess(id, token, request);

            if(validatedAdmin){
                if(adminRepository.existsByName(adminDTO.getName())){
                    errorResponse = new ErrorResponseMessage(HttpStatus.NOT_MODIFIED.value(), "Admin not created.", "Admin with this name already exists.");
                    LOGGER.warning("Admin not created. Admin with name " + adminDTO.getName() + " already exists.");
                    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_MODIFIED);
                } else {
                    adminUpdated = adminServices.update(id, adminDTO);
                    LOGGER.info("Admin " + adminDTO.getName() + " it has been updated.");
                    return new ResponseEntity<>(adminUpdated, HttpStatus.OK);
                }
            } else {
                errorResponse = new ErrorResponseMessage(HttpStatus.NOT_FOUND.value(), "IDENTIFICATION ERROR.", "ERROR: The ID is not found, TOKEN incorrect or wrong ROLE.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        }catch (DuplicateNameException e) {
            errorResponse = new ErrorResponseMessage(HttpStatus.BAD_REQUEST.value(), "Admin not modified.", "Admin with this name already exists.");
            LOGGER.warning("Admin not modified. " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            LOGGER.warning("ERROR: Admin update not possible. " + e.getMessage());
            errorResponse = new ErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "ERROR: Admin update not possible.", "Fail of the game or data base system.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "/deleteUsers/{id}")
    public ResponseEntity<?> RemovesPlayers(@PathVariable("id") Integer id, @RequestBody Confirmation confirmation, HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        try {
            if (adminServices.playerExists(id)) {
            validatedAdmin = adminServices.validateAdminAccessMaster(id, token, request);

            if(validatedAdmin) {
                if (confirmation.isConfirmed()) {
                    adminServices.deletePlayers(id);
                    return new ResponseEntity<>("Player removed successfully.", HttpStatus.OK);
                } else {
                    errorResponse = new ErrorResponseMessage(HttpStatus.OK.value(), "The player has not been removed.", "Be sure to include the 'Yes' parameter in the removal request.");
                    return new ResponseEntity<>(errorResponse, HttpStatus.OK);
                }
            } else {
                errorResponse = new ErrorResponseMessage(HttpStatus.OK.value(), "ERROR ID.", "ID FAILED.");
                return new ResponseEntity<>(errorResponse, HttpStatus.OK);
            }
            } else {
                errorResponse = new ErrorResponseMessage(HttpStatus.NOT_FOUND.value(), "The player has not been removed.", "The player does not exist or has already been removed.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            errorResponse = new ErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "ERROR: The player has not been removed.", "The player has not been removed due to some problem in the game database." + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "/deleteAdmins/{id}")
    public ResponseEntity<?> RemovesAdmins(@PathVariable("id") Integer id, @RequestBody Confirmation confirmation, HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        try{
            if (adminServices.adminExists(id)) {
                validatedAdmin = adminServices.validateAdminAccess(id, token, request);

                if(validatedAdmin) {
                    if(confirmation.isConfirmed()) {
                        adminServices.deleteAdmins(id);
                        return new ResponseEntity<>("Admin removed successfully", HttpStatus.OK);
                    } else {
                        errorResponse = new ErrorResponseMessage(HttpStatus.OK.value(), "The admin has not been removed.", "Be sure to include the 'Yes' parameter in the removal request.");
                        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
                    }
                } else {
                    errorResponse = new ErrorResponseMessage(HttpStatus.NOT_FOUND.value(), "ERROR ID.", "ID FAILED.");
                    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
                }
            } else {
                errorResponse = new ErrorResponseMessage(HttpStatus.OK.value(), "The admin has not been removed.", "The admin does not exist or has already been removed.");
                return new ResponseEntity<>(errorResponse, HttpStatus.OK);
            }
        } catch (Exception e) {
            errorResponse = new ErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "ERROR: The admin has not been removed.", "The admin has not been removed due to some problem in the game database." + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
