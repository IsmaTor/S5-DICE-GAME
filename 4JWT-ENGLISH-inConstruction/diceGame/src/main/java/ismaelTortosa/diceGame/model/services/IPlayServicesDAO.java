package ismaelTortosa.diceGame.model.services;

import ismaelTortosa.diceGame.model.domain.PlayEntity;

import java.util.List;

public interface IPlayServicesDAO {

    public List<Integer> deletePlay(int id); //DELETE /players/{id}/games: removes a player's plays.

    public PlayEntity getPlay(int id); //POST /players/{id}/games: one player play in the game.

    public List<PlayEntity> getOne(int id); //GET /players/{id}/games: return the list of plays of an only player.
}
