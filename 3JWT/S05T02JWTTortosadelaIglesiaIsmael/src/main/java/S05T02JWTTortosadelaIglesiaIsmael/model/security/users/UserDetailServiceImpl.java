package S05T02JWTTortosadelaIglesiaIsmael.model.security.users;

import S05T02JWTTortosadelaIglesiaIsmael.model.domain.UsuarioEntity;
import S05T02JWTTortosadelaIglesiaIsmael.model.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {
        UsuarioEntity usuarioEntity = usuarioRepository
                .findOneByNombre(nombre)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario con nombre " + nombre + " no existe en el juego."));

        return new UserDetailsImpl(usuarioEntity);
    }

    //método para extraer el token
    public static String getToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        //Si el header no existe o no comienza con "Bearer " devuelve null.
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        return header.replace("Bearer ", "");
    }

}
