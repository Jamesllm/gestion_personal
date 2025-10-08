package dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import model.Modulo;

/**
 *
 * @author James
 */
public interface IModuloDAO {

    List<Modulo> obtenerModulosPorRol(int idRol) throws SQLException;

}
