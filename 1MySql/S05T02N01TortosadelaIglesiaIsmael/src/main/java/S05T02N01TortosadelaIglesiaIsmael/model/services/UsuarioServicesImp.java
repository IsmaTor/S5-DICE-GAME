package S05T02N01TortosadelaIglesiaIsmael.model.services;

import S05T02N01TortosadelaIglesiaIsmael.model.domain.JugadaEntity;
import S05T02N01TortosadelaIglesiaIsmael.model.domain.UsuarioEntity;
import S05T02N01TortosadelaIglesiaIsmael.model.dto.UsuarioDTO;
import S05T02N01TortosadelaIglesiaIsmael.model.repository.JugadaRepository;
import S05T02N01TortosadelaIglesiaIsmael.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioServicesImp implements IUsuarioServicesDAO{

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private JugadaRepository jugadaRepository;

    @Override
    public void add(UsuarioDTO usuarioDTO) {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        LocalDate date = LocalDate.now();

        if ( !usuarioRepository.existsByNombre(usuarioDTO.getNombre())) {

            usuarioEntity.setNombre(usuarioDTO.getNombre());
            usuarioEntity.setRegistro(date);
            usuarioEntity.setRegistro(usuarioDTO.getRegistro());

            usuarioRepository.save(usuarioEntity);

        } else {
            System.out.println("ERROR: Nombre de usuario repetido.");
        }
    }
    @Override
    public UsuarioDTO update(int id, UsuarioDTO usuarioDTO) {

        UsuarioEntity usuarioActualizado = null;
        if (!usuarioRepository.existsByNombre(usuarioDTO.getNombre())) {

            UsuarioEntity usuarioEntity = usuarioRepository.findById(id).orElseThrow();
            usuarioEntity.setNombre(usuarioDTO.getNombre());
            usuarioActualizado = usuarioRepository.save(usuarioEntity);
        } else {
            System.out.println("ERROR: Nombre de usuario repetido.");
        }


        return convertEntityToDTO(usuarioActualizado);
    }

    @Override
    public JugadaEntity getJugada(int id) {
        Optional<UsuarioEntity> usuario = usuarioRepository.findById(id);

        UsuarioEntity usuarioEntity = usuario.get();
        JugadaEntity jugadaEntity = new JugadaEntity(usuarioEntity);
        JugadaEntity jugadaCreada = jugadaRepository.save(jugadaEntity);
        calcularPorcentajes(usuarioEntity);

        return jugadaCreada;
    }

    public void calcularPorcentajes(UsuarioEntity usuarioEntity){
        Double porcentaje;
        //Buscar todas las jugadas de un usuario
        Optional<List<JugadaEntity>> jugadas = jugadaRepository.findByUsuarioEntity(usuarioEntity);
        Double numeroDeJugadas = (double) jugadas.get().size();
        //Obtener jugadas ganadas
        List<JugadaEntity> jugadasGanadas = jugadas.get().stream().filter(jugada -> jugada.resultadoJugada() == true).collect(Collectors.toList());
        Double numeroDeJugadasGanadas = (double) jugadasGanadas.size();
        //Calculo de porcentaje
        porcentaje = numeroDeJugadasGanadas * 100 / numeroDeJugadas;
        //Guardar porcentaje en bd
        usuarioEntity.setExito(porcentaje);
        usuarioRepository.save(usuarioEntity);
    }

    @Override
    public List<UsuarioDTO> getAll() {

        return usuarioRepository.findAll().stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public List<JugadaEntity> getOne(int id) {
        UsuarioEntity usuarioEntity = usuarioRepository.findById(id).orElseThrow();

        Optional<List<JugadaEntity>> jugadas = jugadaRepository.findByUsuarioEntity(usuarioEntity);

        jugadas.ifPresent(mostrarJugadas -> mostrarJugadas.forEach( (jugadasUsuario) -> {System.out.println(jugadasUsuario);}));

        return jugadas.orElse(null);
    }

    @Override
    public List<UsuarioDTO> getExitoUp() {
        //Collections.singletonList solo devolverá un objeto de la lista
        //Dobule.compare es para que acepte la comparación entre Doubles
        return Collections.singletonList(usuarioRepository.findAll().stream().map(this::convertEntityToDTO).sorted((up, down) -> Double.compare(down.getExito(), up.getExito())).findFirst().orElse(null));
        //mostrar toda la lista ordenada de mejor a peor
        //return usuarioRepository.findAll().stream().map(this::convertEntityToDTO).sorted((up, down) -> Double.compare(down.getExito(), up.getExito())).collect(Collectors.toList());
    }

    @Override
    public List<UsuarioDTO> getExitoDown() {

        return Collections.singletonList(usuarioRepository.findAll().stream().map(this::convertEntityToDTO).sorted((up, down) -> Double.compare(up.getExito(), down.getExito())).findFirst().orElse(null));
        //mostrar toda la lista ordenada de peor a mejor
        //return usuarioRepository.findAll().stream().map(this::convertEntityToDTO).sorted((up, down) -> Double.compare(up.getExito(), down.getExito())).collect(Collectors.toList());
    }

    @Override
    public Double mediaRanking() {

        double media = usuarioRepository.findAll().stream().mapToDouble(mediaJugadores -> mediaJugadores.getExito()).average().orElse(0.0);
        return media;
    }

    @Override
    public void deleteJugada(int id) {
        UsuarioEntity usuarioEntity = usuarioRepository.findById(id).orElseThrow();

        Optional<List<JugadaEntity>> jugadas = jugadaRepository.findByUsuarioEntity(usuarioEntity);

        jugadas.ifPresent(eliminarJugadas -> eliminarJugadas.forEach(jugadasEliminadas -> jugadaRepository.delete(jugadasEliminadas)));
    }

    //Entity a DTO
    public UsuarioDTO convertEntityToDTO(UsuarioEntity usuarioEntity){
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId_usuario(usuarioEntity.getId_usuario());
        usuarioDTO.setNombre(usuarioEntity.getNombre());
        usuarioDTO.setRegistro(usuarioEntity.getRegistro());
        usuarioDTO.setExito(usuarioEntity.getExito());

        return usuarioDTO;
    }
}
