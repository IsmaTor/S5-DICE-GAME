package ismaelTortosa.diceGame.model.repository;

import ismaelTortosa.diceGame.model.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    boolean existsByName(String name);

    Optional<UserEntity> findOneByName(String name);
}
