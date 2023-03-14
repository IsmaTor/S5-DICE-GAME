package S05T02N01TortosadelaIglesiaIsmael.model.repository;

import S05T02N01TortosadelaIglesiaIsmael.model.domain.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    boolean existsByNombre(String nombre);

}
