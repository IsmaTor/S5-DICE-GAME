package ismaelTortosa.diceGame.model.security.users;

import ismaelTortosa.diceGame.model.domain.UserEntity;
import ismaelTortosa.diceGame.model.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity;

        userEntity = userRepository.findOneByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("The user " + username + " does not exist in the game."));

        return new UserDetailsImpl(userEntity);
    }

    //method to extract the token.
    public static String getToken(HttpServletRequest request) {
        String header;

        header = request.getHeader("Authorization");
        //if the header does not exist or does not start with "Bearer" it returns null.
        if (header == null || !header.startsWith("Bearer")) {
            return null;
        }
        return header.replace("Bearer", "");
    }
}
