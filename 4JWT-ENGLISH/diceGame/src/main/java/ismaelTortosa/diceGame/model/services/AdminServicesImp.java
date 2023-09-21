package ismaelTortosa.diceGame.model.services;

import ismaelTortosa.diceGame.model.domain.AdminEntity;
import ismaelTortosa.diceGame.model.domain.UserEntity;
import ismaelTortosa.diceGame.model.dto.AdminDTO;
import ismaelTortosa.diceGame.model.exceptions.DuplicateNameException;
import ismaelTortosa.diceGame.model.repository.AdminRepository;
import ismaelTortosa.diceGame.model.repository.UserRepository;
import ismaelTortosa.diceGame.model.security.configuration.WebSecurityConfig;
import ismaelTortosa.diceGame.model.security.users.ManagementDetailServiceImpl;
import ismaelTortosa.diceGame.model.security.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AdminServicesImp implements IAdminServices {

    private static final Logger LOGGER = Logger.getLogger(AdminServicesImp.class.getName());
    private AdminEntity adminEntity;

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private WebSecurityConfig webSecurityConfig;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void add(AdminDTO adminDto) {
        adminEntity = new AdminEntity();

        String password;
        String passwordCodified;

        try {
            if (!adminRepository.existsByName(adminDto.getName())) {
                adminEntity.setName(adminDto.getName());

                password = adminDto.getPassword();
                passwordCodified = webSecurityConfig.passwordEncoder().encode(password);
                adminEntity.setPassword(passwordCodified);

                adminRepository.save(adminEntity);
                LOGGER.info("Admin " + adminDto.getName() + " added successfully");
            } else {
                LOGGER.warning("Admin " + adminDto.getName() + " already exists in the game database");
                throw new DuplicateNameException("Admin with name " + adminDto.getName() + " already exists in the game database");
            }
        } catch (Exception e){
            LOGGER.warning("Error adding admin: " + e.getMessage());
        }
    }

    @Override
    public AdminDTO update(int id, AdminDTO adminDTO) {
        AdminEntity adminUpdate = null;

        String password;
        String passwordCodified;

        try{
            if (!adminRepository.existsByName(adminDTO.getName())) {
                adminEntity = adminRepository.findById(id).orElseThrow();
                adminEntity.setName(adminDTO.getName());

                password = adminDTO.getPassword();
                passwordCodified = webSecurityConfig.passwordEncoder().encode(password);
                adminEntity.setPassword(passwordCodified);

                adminUpdate = adminRepository.save(adminEntity);
                LOGGER.info("Admin " + adminDTO.getName() + " updated successfully");
            } else {
                LOGGER.warning("Admin " + adminDTO.getName() + " already exists in the game database");
            }
        } catch (Exception e){
            LOGGER.warning("Error updating admin: " + e.getMessage());
        }
        return convertEntityToDTO(adminUpdate);
    }

    //Entity to DTO
    private AdminDTO convertEntityToDTO(AdminEntity adminEntity){
        AdminDTO adminDTO = new AdminDTO();

        adminDTO.setId_admin(adminEntity.getId_admin());
        adminDTO.setName(adminEntity.getName());

        return adminDTO;
    }

    @Override
    public boolean validateAdminAccess(int id, String token, HttpServletRequest request){
        String roleAdmin;
        int idAdmin;

        token = ManagementDetailServiceImpl.getTokenAdmin(request);

        if (token == null) {
            return false;
        }

        roleAdmin = TokenUtils.getAccessFromTokenRole(token);
        idAdmin = TokenUtils.getUserIdFromToken(token);

        adminEntity = adminRepository.findById(id).orElse(null);

        //if it doesn't find the user or it doesn't match it returns false.
        if (!roleAdmin.equals("admin")) {
            LOGGER.info("ERROR: The validation is incorrect, the admin does not match his ROLE = " + roleAdmin);
            return false;
        } else if (idAdmin != id) {
            LOGGER.info("ERROR: The validation is incorrect, the admin does not match his ID = " + idAdmin);
            return false;
        } else if (adminEntity == null) {
            LOGGER.info("ERROR: The validation is incorrect, the admin not found = " + adminEntity);
            return false;
        }
        LOGGER.info("Validation admin is ok: " + idAdmin + " , " + roleAdmin);
        return true;
    }

    @Override
    public boolean validateAdminAccessMaster(int id, String token, HttpServletRequest request){
        String roleAdmin;

        token = ManagementDetailServiceImpl.getTokenAdmin(request);

        if (token == null) {
            return false;
        }

        roleAdmin = TokenUtils.getAccessFromTokenRole(token);

        if (!roleAdmin.equals("admin")) {
            LOGGER.info("ERROR: The validation is incorrect, the admin does not match his ROLE = " + roleAdmin);
            return false;
        }
        LOGGER.info("Validation admin is ok: " + roleAdmin);
        return true;
    }

    @Override
    public void deletePlayers(int id) {
        UserEntity userEntity;

        try {
            userEntity = userRepository.findById(id).orElseThrow();
            userRepository.delete(userEntity);
            LOGGER.info("Player with ID: " + id + " eliminated.");
        } catch (Exception e){
            LOGGER.warning("Player with ID: " + id + " could not delete" + e.getMessage());
        }
    }

    @Override
    public void deleteAdmins(int id) {
        try {
            adminEntity = adminRepository.findById(id).orElseThrow();
            adminRepository.delete(adminEntity);
            LOGGER.info("Admin with ID: " + id + " eliminated.");
        } catch (Exception e){
            LOGGER.warning("Admin with ID: " + id + " could not delete." + e.getMessage());
        }
    }

    @Override
    public boolean playerExists(Integer id){
        return userRepository.findById(id).isPresent();
    }

    @Override
    public boolean adminExists(Integer id) {
        return adminRepository.findById(id).isPresent();
    }

}

