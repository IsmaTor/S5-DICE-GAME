package S05T02F02TortosadelaIglesiaIsmael.model.services;

import S05T02F02TortosadelaIglesiaIsmael.model.domain.JugadaEntity;
import S05T02F02TortosadelaIglesiaIsmael.model.dto.UsuarioDTO;

import java.util.List;

public interface IUsuarioServicesDAO {
    public void add(UsuarioDTO usuarioDTO); //POST: /players: crea un jugador/a.

    public UsuarioDTO update(String id, UsuarioDTO usuarioDTO); //PUT /players: modifica el nom del jugador/a.

    public void deleteJugada(String id); //DELETE /players/{id}/games: elimina les tirades del jugador/a.

    public JugadaEntity getJugada(String id); //POST /players/{id}/games/ : un jugador/a específic realitza una tirada dels daus.

    public List<UsuarioDTO> getAll(); //GET /players/: retorna el llistat de tots  els jugadors/es del sistema amb el seu  percentatge mitjà d’èxits.

    public List<JugadaEntity> getOne(String id); //GET /players/{id}/games: retorna el llistat de jugades per un jugador/a.

    public List<UsuarioDTO> getExitoUp(); //GET /players/ranking/winner: retorna el  jugador amb mijor percentatge d’èxit.

    public List<UsuarioDTO> getExitoDown(); //GET /players/ranking/loser: retorna el jugador/a  amb pitjor percentatge d’èxit.

    public Double mediaRanking(); //GET /players/ranking: retorna el ranking mig de tots els jugadors/es del sistema. És a dir, el  percentatge mitjà d’èxits.
}
