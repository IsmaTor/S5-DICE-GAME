package ismaelTortosa.diceGame.model.repository;

import ismaelTortosa.diceGame.model.domain.PlayEntity;
import ismaelTortosa.diceGame.model.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayRepository extends JpaRepository<PlayEntity, Integer> {

    //Search for a user's plays
    Optional<List<PlayEntity>> findByUserEntity(UserEntity userEntity);
}
