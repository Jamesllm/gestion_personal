package dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import model.Departamento;

/**
 *
 * @author James
 */
public interface IDepartamentoDAO {
    void crearDepartamento(Departamento departamento) throws SQLException;
    void actualizarDepartamento(Departamento departamento) throws SQLException;
    void eliminarDepartamento(int id_departamento) throws SQLException;
    
    List<Departamento> obtenerTodosDepartamentos() throws SQLException;
    Departamento obtenerDepartamentoPorId(int id_departamento) throws SQLException;
    List<String[]> obtenerDistribucionDepartamentos();
}
