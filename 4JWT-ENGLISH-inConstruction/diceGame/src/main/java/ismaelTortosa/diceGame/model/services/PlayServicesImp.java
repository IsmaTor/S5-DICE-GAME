package ismaelTortosa.diceGame.model.services;

import ismaelTortosa.diceGame.model.domain.PlayEntity;
import ismaelTortosa.diceGame.model.domain.UserEntity;
import ismaelTortosa.diceGame.model.dto.UserDTO;
import ismaelTortosa.diceGame.model.repository.PlayRepository;
import ismaelTortosa.diceGame.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class PlayServicesImp implements IPlayServicesDAO{

    private static final Logger LOGGER = Logger.getLogger(PlayServicesImp.class.getName());
    @Autowired
    private PlayRepository playRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public PlayEntity getPlay(int id) {
        Optional<UserEntity> user;
        UserEntity userEntity;
        PlayEntity playEntity, playCreated = null;
        try{
            user = userRepository.findById(id);
            userEntity = user.get();
            LOGGER.info("Player " + userEntity + " is playing.");
            playEntity = new PlayEntity(userEntity);
            playCreated = playRepository.save(playEntity);
            calculatedAverage(userEntity);
        } catch (Exception e){
            LOGGER.warning("The game was not possible. " + e);
        }

        return playCreated;
    }

    @Override
    public void deletePlay(int id, UserDTO userDTO) {
        UserEntity userEntity;
        Optional<List<PlayEntity>> plays;
        try{
            userEntity = userRepository.findById(id).orElseThrow();
            plays = playRepository.findByUserEntity(userEntity);

            plays.ifPresent(deletePlay -> deletePlay.forEach(playsDeletes -> playRepository.delete(playsDeletes)));
            LOGGER.info("User " + userEntity + " plays have been removed.");
        } catch (Exception e){
            LOGGER.warning("Could not delete plays." + e);
        }

    }

    @Override
    public List<PlayEntity> getOne(int id) {
        UserEntity userEntity;
        Optional<List<PlayEntity>> plays;

        userEntity = userRepository.findById(id).orElseThrow();
        plays = playRepository.findByUserEntity(userEntity);

        plays.ifPresent(showPlays -> showPlays.forEach((playsUser) -> {LOGGER.info("Showing games: " + playsUser);}));

        return plays.orElse(null);
    }

    private void calculatedAverage(UserEntity userEntity){
        Double average, numberOfPlays, numberOfPlaysWon;
        Optional<List<PlayEntity>> plays;
        List<PlayEntity> playsWon;

        //Search all the plays of a user.
        plays = playRepository.findByUserEntity(userEntity);
        numberOfPlays = (double) plays.get().size();
        //Get plays won.
        playsWon = plays.get().stream().filter(playUser -> playUser.playResult() == true).collect(Collectors.toList());
        numberOfPlaysWon = (double) playsWon.size();
        //Percentage calculation.
        average = numberOfPlaysWon * 100 / numberOfPlays;
        //Save average in bd.
        userEntity.setWinner(average);
        userRepository.save(userEntity);

    }
}
