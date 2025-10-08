package dao.interfaces;

import java.util.List;
import model.Usuario;

/**
 *
 * @author James
 */
public interface IUsuarioDAO {

    Usuario login(String username, String password);
    boolean cambiarPassword(int idUsuario, String nuevaContrasena);
    Usuario obtenerPorId(int idUsuario);
    
    List<Usuario> listarTodos();
    
}
