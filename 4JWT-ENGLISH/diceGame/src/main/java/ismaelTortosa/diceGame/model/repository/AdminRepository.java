package ismaelTortosa.diceGame.model.repository;

import ismaelTortosa.diceGame.model.domain.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Integer> {

    boolean existsByName(String name);

    Optional<AdminEntity> findOneByName(String name);

}
