package dao.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;
import model.Asistencia;

/**
 *
 * @author James
 */
public interface IAsistenciaDAO {
    void registrarEntrada(Asistencia asistencia) throws SQLException;
    void registrarSalida(int idAsistencia, LocalTime horaSalida, String estado) throws SQLException;
    
    List<Asistencia> obtenerTodas() throws SQLException;
    List<Asistencia> obtenerTodasUsuario(int idEmpleado) throws SQLException;
    int obtenerAsistenciaActiva(int idEmpleado) throws SQLException;
    List<Asistencia> obtenerPorEmpleado(int idEmpleado) throws SQLException;
    List<Asistencia> obtenerActividadReciente(int limite) throws SQLException;
    
    Asistencia mapearAsistencia(ResultSet rs) throws SQLException;
    List<String[]> obtenerAsistenciaMensual();
}
