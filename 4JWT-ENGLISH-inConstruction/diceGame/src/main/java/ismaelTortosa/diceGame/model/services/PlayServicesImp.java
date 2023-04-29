package ismaelTortosa.diceGame.model.services;

import ismaelTortosa.diceGame.model.domain.PlayEntity;
import ismaelTortosa.diceGame.model.domain.UserEntity;
import ismaelTortosa.diceGame.model.repository.PlayRepository;
import ismaelTortosa.diceGame.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class PlayServicesImp implements IPlayServicesDAO{

    private static final Logger LOGGER = Logger.getLogger(PlayServicesImp.class.getName());

    //variables
    private UserEntity userEntity;
    private Optional<List<PlayEntity>> plays;

    @Autowired
    private PlayRepository playRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public PlayEntity getPlay(int id) {
        PlayEntity playEntity, playCreated = null;
        try{
            userEntity = userRepository.findById(id).orElseThrow();
            LOGGER.info("Player " + userEntity + " is playing.");
            playEntity = new PlayEntity(userEntity);
            playCreated = playRepository.save(playEntity);
            calculatedAverage(userEntity);
        } catch (Exception e){
            LOGGER.warning("The game was not possible. " + e.getMessage());
        }
        return playCreated;
    }

    @Override
    public List<Integer> deletePlay(int id) {

        List<Integer> deletedPlayIds = new ArrayList<>();
        try {
            userEntity = userRepository.findById(id).orElseThrow();
            plays = playRepository.findByUserEntity(userEntity);

            plays.ifPresent(deletePlay -> deletePlay.forEach(playsDeletes -> {
                deletedPlayIds.add(playsDeletes.getId_play());
                playRepository.delete(playsDeletes);}));

            LOGGER.info("User " + userEntity.getName() + " plays have been removed.");
        } catch (Exception e) {
            LOGGER.warning("Could not delete plays." + e.getMessage());
        }
        return deletedPlayIds;
    }

    @Override
    public List<PlayEntity> getOne(int id) {

        userEntity = userRepository.findById(id).orElseThrow();
        plays = playRepository.findByUserEntity(userEntity);

        //Control
        plays.ifPresent(showPlays -> showPlays.forEach((playsUser) -> {LOGGER.info("Showing games: " + playsUser);}));

        //Check if the list is empty.
        if (plays.isPresent() && plays.get().isEmpty()) {
            throw new IllegalStateException("The games list is empty.");
        }
        //Check if the list is null and return list.
        return plays.orElseThrow(() -> new IllegalStateException("The games list in null."));
    }

    private void calculatedAverage(UserEntity userEntity){
        Double average, numberOfPlays, numberOfPlaysWon;
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
