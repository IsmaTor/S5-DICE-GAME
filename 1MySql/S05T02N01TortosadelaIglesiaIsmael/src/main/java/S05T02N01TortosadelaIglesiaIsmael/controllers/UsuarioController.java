package S05T02N01TortosadelaIglesiaIsmael.controllers;

import S05T02N01TortosadelaIglesiaIsmael.model.domain.JugadaEntity;
import S05T02N01TortosadelaIglesiaIsmael.model.dto.UsuarioDTO;
import S05T02N01TortosadelaIglesiaIsmael.model.repository.UsuarioRepository;
import S05T02N01TortosadelaIglesiaIsmael.model.services.IUsuarioServicesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/players")
public class UsuarioController {
    Logger logger = Logger.getLogger(UsuarioDTO.class.toString());

    @Autowired
    private IUsuarioServicesDAO usuarioServices;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping(path= "/add") //http://localhost:9001/playeres/add
    public ResponseEntity<?> addUsuario(@RequestBody UsuarioDTO usuarioDTO){

        try {
            usuarioServices.add(usuarioDTO);
            logger.info("Usuario " + usuarioDTO.getNombre() + " registrado ");
            return new ResponseEntity<>(usuarioDTO, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>("No se han encotrado jugadores en el juego.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path= "/update/{id}") //http://localhost:9001/players/update/?
    public ResponseEntity<?> updateUsuario(@PathVariable("id") Integer id, @RequestBody UsuarioDTO usuarioDTO){
        try {
            UsuarioDTO usuario = usuarioServices.update(id, usuarioDTO);
            logger.info("Usuario " + usuarioDTO.getNombre() + " ha sido modificado.");
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("No se han encotrado jugadores en el juego.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path= "/game/{id}") //http://localhost:9001/players/game/?
    public ResponseEntity <JugadaEntity> partidaDados (@PathVariable("id") int id){
        try {
            JugadaEntity lanzaDados = usuarioServices.getJugada(id);
            logger.info("Dado 1: " + lanzaDados.getDado1() + ", Dado 2: " + lanzaDados.getDado2() + " = " + lanzaDados.resultadoJugada());
            return new ResponseEntity<>(lanzaDados, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path= "/getAll") //http://localhost:9001/players/getAll
    public ResponseEntity<?> getAllJugadores() {
        try {
            List<UsuarioDTO> usuarios = usuarioServices.getAll();
            logger.info("Lista de usuarios:  " + usuarios.toString());
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("No se han encotrado jugadores en el juego.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path= "/delete/{id}") //http://localhost:9001/players/delete/?
    public ResponseEntity<?> deleteJugada(@PathVariable("id") int id ) {
        try {
            usuarioServices.deleteJugada(id);
            logger.info("Eliminando jugadas de un jugador.");
            return new ResponseEntity<>("Jugadas eliminadas correctamente", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Jugadas no encontradas", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path= "/getOne/{id}") //http://localhost:9001/players/getOne/?
    public ResponseEntity<?> getOneJugador(@PathVariable("id") int id) {
        try {
            List<JugadaEntity> jugadas = usuarioServices.getOne(id);
            logger.info("Lista de jugadas: " + jugadas);
            return new ResponseEntity<>(jugadas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Jugadas no encontradas", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path= "/getUp") //http://localhost:9001/players/getUp
    public ResponseEntity<?> getUpJugador() {
        try {
            List<UsuarioDTO> rankingUp = usuarioServices.getExitoUp();
            logger.info("1era Posición: " + rankingUp);
            return new ResponseEntity<>(rankingUp, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Ranking no encontrado", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path= "/getDown") //http://localhost:9001/players/getDown
    public ResponseEntity<?> getDownJugador() {
        try {
            List<UsuarioDTO> rankingDown = usuarioServices.getExitoDown();
            logger.info("Última posición: " + rankingDown);
            return new ResponseEntity<>(rankingDown, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Ranking no encontrado", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path= "/getMedia") //http://localhost:9001/players/getMedia
    public ResponseEntity<?> getMediaJugadores() {
        try {
            Double media = usuarioServices.mediaRanking();
            logger.info("Media de todos los jugadores del juego: " + media + " %");
            return new ResponseEntity<>(media + " %", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Porcentajes no encontrados", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
