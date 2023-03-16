package ismaelTortosa.diceGame.model.services;

import ismaelTortosa.diceGame.model.domain.UserEntity;
import ismaelTortosa.diceGame.model.dto.UserDTO;
import ismaelTortosa.diceGame.model.repository.UserRepository;
import ismaelTortosa.diceGame.model.security.configuration.WebSecurityConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;


@Service
public class UserServicesImp implements IUserServicesDAO {
    private static final Logger LOGGER = Logger.getLogger(UserServicesImp.class.getName());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WebSecurityConfig webSecurityConfig;

    @Override
    public void add(UserDTO userDto) {
        UserEntity userEntity = new UserEntity();
        LocalDate date = LocalDate.now();
        String password;
        String passwordCodified;

        try {
            if (!userRepository.existsByName(userDto.getName())) {
                userEntity.setName(userDto.getName());
                userEntity.setRegister(date);
                userEntity.setRegister(userDto.getRegister());

                password = userDto.getPassword();
                passwordCodified = webSecurityConfig.passwordEncoder().encode(password);
                userEntity.setPassword(passwordCodified);

                userRepository.save(userEntity);
                LOGGER.info("User " + userDto.getName() + " user added successfully");
            } else {
                LOGGER.warning("User " + userDto.getName() + " already exists in the game database");
            }
        } catch (Exception e){
            LOGGER.info("Error adding user: " + e.getMessage());
        }

    }

    @Override
    public UserDTO update(int id, UserDTO userDTO) {
        return null;
    }

    @Override
    public List<UserDTO> getAll() {
        return null;
    }

    @Override
    public List<UserDTO> getWinnerUp() {
        return null;
    }

    @Override
    public List<UserDTO> getWinnerDown() {
        return null;
    }

    @Override
    public Double averageRanking() {
        return null;
    }

    @Override
    public boolean validationToken(int id, HttpServletRequest request) {
        return false;
    }
}
