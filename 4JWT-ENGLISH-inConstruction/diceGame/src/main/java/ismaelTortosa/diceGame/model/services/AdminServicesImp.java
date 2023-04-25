package ismaelTortosa.diceGame.model.services;

import ismaelTortosa.diceGame.model.domain.AdminEntity;
import ismaelTortosa.diceGame.model.dto.AdminDTO;
import ismaelTortosa.diceGame.model.exceptions.DuplicateNameException;
import ismaelTortosa.diceGame.model.repository.AdminRepository;
import ismaelTortosa.diceGame.model.security.configuration.WebSecurityConfig;
import ismaelTortosa.diceGame.model.security.users.ManagementDetailServiceImpl;
import ismaelTortosa.diceGame.model.security.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AdminServicesImp implements IAdminServicesDAO{

    private static final Logger LOGGER = Logger.getLogger(AdminServicesImp.class.getName());

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private WebSecurityConfig webSecurityConfig;

    @Override
    public void add(AdminDTO adminDto) {
        AdminEntity adminEntity = new AdminEntity();

        String password;
        String passwordCodified;

        try {
            if (!adminRepository.existsByName(adminDto.getName())) {
                adminEntity.setName(adminDto.getName());

                password = adminDto.getPassword();
                passwordCodified = webSecurityConfig.passwordEncoder().encode(password);
                adminEntity.setPassword(passwordCodified);

                adminRepository.save(adminEntity);
                LOGGER.info("User " + adminDto.getName() + " added successfully");
            } else {
                LOGGER.warning("User " + adminDto.getName() + " already exists in the game database");
                throw new DuplicateNameException("User with name " + adminDto.getName() + " already exists in the game database");
            }
        } catch (Exception e){
            LOGGER.warning("Error adding user: " + e.getMessage());
        }
    }

    @Override
    public AdminDTO update(int id, AdminDTO adminDTO) {
        AdminEntity userUpdate = null;
        AdminEntity userEntity;

        String password;
        String passwordCodified;

        try{
            if (!adminRepository.existsByName(adminDTO.getName())) {
                userEntity = adminRepository.findById(id).orElseThrow();
                userEntity.setName(adminDTO.getName());

                password = adminDTO.getPassword();
                passwordCodified = webSecurityConfig.passwordEncoder().encode(password);
                userEntity.setPassword(passwordCodified);

                userUpdate = adminRepository.save(userEntity);
                LOGGER.info("User " + adminDTO.getName() + " updated successfully");
            } else {
                LOGGER.warning("User " + adminDTO.getName() + " already exists in the game database");
            }
        } catch (Exception e){
            LOGGER.warning("Error updating user: " + e.getMessage());
        }
        return convertEntityToDTO(userUpdate);
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
        AdminEntity adminEntity;
        String rolUser;

        token = ManagementDetailServiceImpl.getTokenAdmin(request);

        if (token == null) {
            return false;
        }

        rolUser = TokenUtils.getAccessFromToken(token);
        adminEntity = adminRepository.findById(id).orElse(null);

        //if it doesn't find the user or it doesn't match it returns false.
        if (adminEntity == null || !rolUser.equals("admin")) {
            LOGGER.info("ERROR: The validation is incorrect, the user does not match his ID!");
            return false;
        }
        return true;
    }

    @Override
    public boolean validationTokenId(int id, HttpServletRequest request) {
        AdminEntity adminEntity;
        String token;
        int idAdmin;

        token = ManagementDetailServiceImpl.getTokenAdmin(request);
        if (token == null) {
            return false;
        }

        idAdmin = TokenUtils.getUserIdFromToken(token);
        adminEntity = adminRepository.findById(id).orElse(null);

        //if it doesn't find the user or it doesn't match it returns false.
        if (adminEntity == null || idAdmin != id) {
            LOGGER.info("ERROR: The validation is incorrect, the user does not match his ID!");
            return false;
        }
        return true;
    }

}

