package dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import model.Empleado;

/**
 *
 * @author James
 */
public interface IEmpleadoDAO {

    // CRUD
    void crearEmpleado(Empleado empleado) throws SQLException;
    List<Empleado> obtenerTodosEmpleados() throws SQLException;
    Empleado obtenerEmpleadoPorId(int id_empleado) throws SQLException;
    void actualizarEmpleado(Empleado empleado) throws SQLException;
    void eliminarEmpleado(int id_empleado) throws SQLException;

    // Métricas y estadísticas
    int obtenerTotalEmpleados() throws SQLException;
    int obtenerTotalEmpleadosPresentesHoy() throws SQLException;
    double obtenerHorasTotalesHoy() throws SQLException;
    int obtenerTotalAusentesHoy() throws SQLException;
    double obtenerPromedioHorasHoy() throws SQLException;

    // Reportes y distribución
    List<String> obtenerActividadReciente(int limite) throws SQLException;
    Map<String, Integer> obtenerDistribucionPorDepartamento() throws SQLException;
    Map<String, Integer> obtenerAsistenciaMensual() throws SQLException;

    // Búsquedas
    int obtenerIdPorDni(String dni) throws SQLException;
}
