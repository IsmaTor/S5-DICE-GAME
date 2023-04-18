package ismaelTortosa.diceGame.model.security.users;

import ismaelTortosa.diceGame.model.domain.AdminEntity;
import ismaelTortosa.diceGame.model.domain.UserEntity;
import ismaelTortosa.diceGame.model.repository.AdminRepository;
import ismaelTortosa.diceGame.model.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ManagementDetailServiceImpl implements UserDetailsService {

    @Autowired
    AdminRepository adminRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AdminEntity> adminOptional = adminRepository.findOneByName(username);
        if (adminOptional.isPresent()) { //Access for administrator
            AdminEntity adminEntity = adminOptional.get();
            return new AdminDetailsImpl(adminEntity);
        } else { //Access for user.
            Optional<UserEntity> userOptional = userRepository.findOneByName(username);
            if (userOptional.isPresent()) {
                UserEntity userEntity = userOptional.get();
                return new UserDetailsImpl(userEntity);
            } else {
                throw new UsernameNotFoundException("The user " + username + " does not exist in the game.");
            }
        }
    }

    //method to extract the token.
    public static String getTokenAdmin(HttpServletRequest request) {
        String header;

        header = request.getHeader("Authorization");
        //if the header does not exist or does not start with "Bearer" it returns null.
        if (header == null || !header.startsWith("Bearer")) {
            return null;
        }
        return header.replace("Bearer", "");
    }

}

