package ismaelTortosa.diceGame.model.services;

import ismaelTortosa.diceGame.model.domain.PlayEntity;
import ismaelTortosa.diceGame.model.dto.UserDTO;

import java.util.List;

public interface IPlayServicesDAO {

    public void deletePlay(int id, UserDTO userDTO); //DELETE /players/{id}/games: removes a player's plays.

    public PlayEntity getPlay(int id); //POST /players/{id}/games: one player play in the game.

    public List<PlayEntity> getOne(int id); //GET /players/{id}/games: return the list of plays of an only player.
}
