package S05T02JWTTortosadelaIglesiaIsmael.model.repository;

import S05T02JWTTortosadelaIglesiaIsmael.model.domain.JugadaEntity;
import S05T02JWTTortosadelaIglesiaIsmael.model.domain.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JugadaRepository extends JpaRepository<JugadaEntity, Integer> {

    //Busca las jugadas de un usuario.
    Optional<List<JugadaEntity>> findByUsuarioEntity(UsuarioEntity usuarioEntity);
}
