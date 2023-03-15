package ismaelTortosa.diceGame.model.services;

import ismaelTortosa.diceGame.model.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface IUserServicesDAO {

    public void add(UserDTO userDto); //POST /players: create a user.

    public UserDTO update(int id, UserDTO userDTO); //PUT /players: change players names.

    public List<UserDTO> getAll(); //GET /players/{id}/games: returns the list of moves for a player's game.

    public List<UserDTO> getWinnerUp(); //GET /players/ranking/winner: returns the players with the highest percentage of games won.

    public List<UserDTO> getWinnerDown(); //GET /players/ranking/loser: returns the players with the lowest percentage of games won.

    public Double averageRanking(); //GET /players/ranking: returns the average ranking of the all players.

    boolean validationToken(int id, HttpServletRequest request); //validate player's by comparing the token id with user id.
}
