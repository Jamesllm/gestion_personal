package dao.impl;

import com.google.common.base.Preconditions;
import dao.interfaces.IAsistenciaDAO;
import model.Asistencia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla Asistencia
 *
 * @author James
 */
public class AsistenciaDAOImpl implements IAsistenciaDAO {

    private static final Logger logger = LoggerFactory.getLogger(AsistenciaDAOImpl.class);
    private final Connection conexion;

    public AsistenciaDAOImpl(Connection conexion) {
        this.conexion = Preconditions.checkNotNull(conexion, "La conexión no puede ser nula");
    }

    // Insertar nueva asistencia (entrada)
    @Override
    public void registrarEntrada(Asistencia asistencia) throws SQLException {
        Preconditions.checkNotNull(asistencia, "La asistencia no puede ser nula");

        String sql = "INSERT INTO asistencia (id_empleado, hora_entrada, hora_salida, estado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, asistencia.getIdEmpleado());
            ps.setTime(2, asistencia.getHoraEntrada());
            ps.setTime(3, asistencia.getHoraSalida());
            ps.setString(4, asistencia.getEstado());
            ps.executeUpdate();
            logger.info("Entrada registrada para empleado ID {}", asistencia.getIdEmpleado());
        } catch (SQLException e) {
            logger.error("Error al registrar entrada: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Registrar salida
    @Override
    public void registrarSalida(int idAsistencia, LocalTime horaSalida, String estado) throws SQLException {
        Preconditions.checkArgument(idAsistencia > 0, "El ID de asistencia debe ser válido");
        Preconditions.checkNotNull(horaSalida, "La hora de salida no puede ser nula");

        String sql = "UPDATE asistencia SET hora_salida = ?, estado = ? WHERE id_asistencia = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setTime(1, Time.valueOf(horaSalida));
            ps.setString(2, estado);
            ps.setInt(3, idAsistencia);
            ps.executeUpdate();
            logger.info("Salida registrada para asistencia ID {}", idAsistencia);
        } catch (SQLException e) {
            logger.error("Error al registrar salida: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Obtener todas las asistencias
    @Override
    public List<Asistencia> obtenerTodas() throws SQLException {
        List<Asistencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM asistencia ORDER BY fecha DESC, hora_entrada DESC";
        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearAsistencia(rs));
            }
            logger.info("Se obtuvieron {} registros de asistencia", lista.size());
        }
        return lista;
    }

    @Override
    public List<Asistencia> obtenerTodasUsuario(int idEmpleado) throws SQLException {
        Preconditions.checkArgument(idEmpleado > 0, "El ID de empleado debe ser válido");

        List<Asistencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM asistencia WHERE id_empleado = ? ORDER BY fecha DESC, hora_entrada DESC";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearAsistencia(rs));
                }
            }
            logger.info("Asistencias obtenidas para empleado ID {}", idEmpleado);
        }
        return lista;
    }

    @Override
    public int obtenerAsistenciaActiva(int idEmpleado) throws SQLException {
        Preconditions.checkArgument(idEmpleado > 0, "El ID de empleado debe ser válido");

        String sql = "SELECT id_asistencia FROM asistencia WHERE id_empleado = ? AND hora_salida IS NULL";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id_asistencia");
                    logger.info("Asistencia activa encontrada: ID {}", id);
                    return id;
                }
            }
        }
        return -1;
    }

    @Override
    public List<Asistencia> obtenerPorEmpleado(int idEmpleado) throws SQLException {
        Preconditions.checkArgument(idEmpleado > 0, "El ID de empleado debe ser válido");

        List<Asistencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM asistencia WHERE id_empleado = ? ORDER BY fecha DESC";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearAsistencia(rs));
                }
            }
            logger.info("Se obtuvieron {} asistencias para empleado {}", lista.size(), idEmpleado);
        }
        return lista;
    }

    @Override
    public List<Asistencia> obtenerActividadReciente(int limite) throws SQLException {
        Preconditions.checkArgument(limite > 0, "El límite debe ser mayor a 0");

        List<Asistencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM asistencia ORDER BY fecha DESC, hora_entrada DESC LIMIT ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearAsistencia(rs));
                }
            }
            logger.info("Se obtuvieron las {} asistencias más recientes", limite);
        }
        return lista;
    }

    @Override
    public Asistencia mapearAsistencia(ResultSet rs) throws SQLException {
        return new Asistencia(
                rs.getInt("id_asistencia"),
                rs.getInt("id_empleado"),
                rs.getDate("fecha"),
                rs.getTime("hora_entrada"),
                rs.getTime("hora_salida"),
                rs.getString("estado")
        );
    }

    @Override
    public List<String[]> obtenerAsistenciaMensual() {
        List<String[]> datos = new ArrayList<>();
        String sql = "SELECT MONTH(fecha) AS mes, COUNT(*) AS total_asistencias FROM asistencia GROUP BY MONTH(fecha) ORDER BY mes";
        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                datos.add(new String[]{String.valueOf(rs.getInt("mes")), String.valueOf(rs.getInt("total_asistencias"))});
            }
            logger.info("Datos mensuales de asistencia cargados correctamente");
        } catch (SQLException e) {
            logger.error("Error al obtener asistencia mensual: {}", e.getMessage(), e);
        }
        return datos;
    }
}
