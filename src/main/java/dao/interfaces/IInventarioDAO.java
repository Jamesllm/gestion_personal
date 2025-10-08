package dao.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import model.Inventario;

/**
 *
 * @author James
 */
public interface IInventarioDAO {
    boolean insertar(Inventario inv) throws SQLException;
    boolean actualizar(Inventario inv) throws SQLException;
    boolean eliminar(int idItem) throws SQLException;
    
    Inventario buscarPorId(int idItem) throws SQLException;
    List<Inventario> listar() throws SQLException;
    Inventario mapResultSet(ResultSet rs) throws SQLException;
}
