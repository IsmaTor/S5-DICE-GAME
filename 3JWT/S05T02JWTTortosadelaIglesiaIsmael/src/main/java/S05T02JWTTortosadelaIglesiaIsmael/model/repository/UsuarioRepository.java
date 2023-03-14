package S05T02JWTTortosadelaIglesiaIsmael.model.repository;

import S05T02JWTTortosadelaIglesiaIsmael.model.domain.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    boolean existsByNombre(String nombre);

    Optional<UsuarioEntity> findOneByNombre(String nombre);

}