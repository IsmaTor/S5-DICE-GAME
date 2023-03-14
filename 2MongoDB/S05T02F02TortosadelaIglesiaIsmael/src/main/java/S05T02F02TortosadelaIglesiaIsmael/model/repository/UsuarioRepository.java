package S05T02F02TortosadelaIglesiaIsmael.model.repository;

import S05T02F02TortosadelaIglesiaIsmael.model.domain.UsuarioEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends MongoRepository<UsuarioEntity, String> {
    boolean existsByNombre(String nombre);
}
