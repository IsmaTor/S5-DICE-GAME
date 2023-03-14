package S05T02F02TortosadelaIglesiaIsmael.model.repository;

import S05T02F02TortosadelaIglesiaIsmael.model.domain.JugadaEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JugadaRepository extends MongoRepository<JugadaEntity, String> {
    //Busca las jugadas de un usuario.
    //Optional<List<JugadaEntity>> findByUsuarioEntity(UsuarioEntity usuarioEntity);

    Optional<List<JugadaEntity>> findByIdUsuario(String idUsuario);
}
