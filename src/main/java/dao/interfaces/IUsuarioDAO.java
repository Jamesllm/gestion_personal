package dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import model.Usuario;

/**
 *
 * @author James
 */
public interface IUsuarioDAO {

    Usuario login(String username, String password) throws SQLException;
    boolean insertar(Usuario usuario) throws SQLException;
    boolean actualizar(Usuario usuario) throws SQLException;
    boolean cambiarPassword(int idUsuario, String nuevaContrasena) throws SQLException;
    boolean eliminar(int idUsuario) throws SQLException;
    Usuario obtenerPorId(int idUsuario) throws SQLException;
    
    List<Usuario> listarTodos() throws SQLException;
    
}
