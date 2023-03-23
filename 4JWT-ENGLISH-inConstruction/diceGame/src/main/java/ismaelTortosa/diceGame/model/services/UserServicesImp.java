package ismaelTortosa.diceGame.model.services;

import ismaelTortosa.diceGame.model.domain.UserEntity;
import ismaelTortosa.diceGame.model.dto.UserDTO;
import ismaelTortosa.diceGame.model.repository.UserRepository;
import ismaelTortosa.diceGame.model.security.configuration.WebSecurityConfig;
import ismaelTortosa.diceGame.model.security.users.UserDetailServiceImpl;
import ismaelTortosa.diceGame.model.security.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


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
                LOGGER.info("User " + userDto.getName() + " added successfully");
            } else {
                LOGGER.warning("User " + userDto.getName() + " already exists in the game database");
            }
        } catch (Exception e){
            LOGGER.info("Error adding user: " + e.getMessage());
        }

    }

    @Override
    public UserDTO update(int id, UserDTO userDTO) {
        UserEntity userUpdate = null;
        UserEntity userEntity;
        String password;
        String passwordCodified;

        try{
            if (!userRepository.existsByName(userDTO.getName())) {
                userEntity = userRepository.findById(id).orElseThrow();
                userEntity.setName(userDTO.getName());

                password = userDTO.getPassword();
                passwordCodified = webSecurityConfig.passwordEncoder().encode(password);
                userEntity.setPassword(passwordCodified);

                userUpdate = userRepository.save(userEntity);
                LOGGER.info("User " + userDTO.getName() + " updated successfully");
            } else {
                LOGGER.warning("User " + userDTO.getName() + " already exists in the game database");
            }
        } catch (Exception e){
            LOGGER.info("Error updating user: " + e.getMessage());
        }
        return convertEntityToDTO(userUpdate);
    }

    @Override
    public List<UserDTO> getAll() {
        List<UserDTO> getAllUsers;

        getAllUsers = userRepository.findAll().stream().map(this::convertEntityToDTO).collect(Collectors.toList());

        return getAllUsers;
    }

    @Override
    public List<UserDTO> getWinnerUp() {
        //"Collections.singletonList" will only return a list object.
        //Creates a comparer that sorts the UserDTO objects by the value of the winner property in descending order.
        List<UserDTO> getWinnerUp;

        getWinnerUp = Collections.singletonList(userRepository.findAll().stream().map(this::convertEntityToDTO)
                .sorted(Comparator.comparingDouble(UserDTO::getWinner).reversed())
                .findFirst().orElse(null));

        return getWinnerUp;
    }

    @Override
    public List<UserDTO> getAllUp(){
        List<UserDTO> getAllUp;

        getAllUp = userRepository.findAll().stream().map(this::convertEntityToDTO)
                .sorted(Comparator.comparingDouble(UserDTO::getWinner).reversed())
                .collect(Collectors.toList());

        return getAllUp;
    }

    @Override
    public List<UserDTO> getWinnerDown() {
        //"Collections.singletonList" will only return a list object.
        //Creates a comparer that sorts the UserDTO objects by the value of the winner property in ascendant order.
        List<UserDTO> getWinnerDown;

        getWinnerDown = Collections.singletonList(userRepository.findAll().stream().map(this::convertEntityToDTO)
                .sorted(Comparator.comparingDouble(UserDTO::getWinner))
                .findFirst().orElse(null));

        return getWinnerDown;
    }

    @Override
    public List<UserDTO> getAllDown(){
        List<UserDTO> getAllDown;

        getAllDown = userRepository.findAll().stream().map(this::convertEntityToDTO)
                .sorted(Comparator.comparingDouble(UserDTO::getWinner))
                .collect(Collectors.toList());

        return getAllDown;
    }

    @Override
    public Double averageRanking() {
        double average;

        average = userRepository.findAll().stream()
                .mapToDouble(playersAverage -> playersAverage.getWinner())
                .average()
                .orElse(0.0);

        return average;
    }

    //Validate the token by comparing the token id with the user id.
    @Override
    public boolean validationToken(int id, HttpServletRequest request) {
        UserEntity userEntity;
        String token;
        int idUser;

        token = UserDetailServiceImpl.getToken(request);
        if (token == null) {
            return false;
        }

        idUser = TokenUtils.getUserIdFromToken(token);
        userEntity = userRepository.findById(id).orElse(null);

        //if it doesn't find the user or it doesn't match it returns false.
        if (userEntity == null || idUser != id) {
            LOGGER.info("ERROR: The validation is incorrect, the user does not match his ID!");
            return false;
        }
        return true;
    }

    //Entity to DTO
    private UserDTO convertEntityToDTO(UserEntity userEntity){
       UserDTO userDTO = new UserDTO();

       userDTO.setId_user(userEntity.getId_user());
       userDTO.setName(userEntity.getName());
       userDTO.setRegister(userEntity.getRegister());
       userDTO.setWinner(userEntity.getWinner());

       return userDTO;
    }
}
