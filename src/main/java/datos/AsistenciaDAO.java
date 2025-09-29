package datos;

import clases.Asistencia;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla Asistencia
 *
 * @author James
 */
public class AsistenciaDAO {

    private final Connection conexion;

    public AsistenciaDAO(Connection conexion) {
        this.conexion = conexion;
    }

    // Insertar nueva asistencia (entrada)
    public void registrarEntrada(Asistencia asistencia) throws SQLException {
        String sql = "INSERT INTO asistencia (id_empleado, fecha, hora_entrada, estado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, asistencia.getIdEmpleado());
            ps.setDate(2, Date.valueOf(asistencia.getFecha()));
            ps.setTime(3, Time.valueOf(asistencia.getHoraEntrada()));
            ps.setString(4, asistencia.getEstado());
            ps.executeUpdate();
        }
    }

    // Registrar salida
    public void registrarSalida(int idAsistencia, LocalTime horaSalida, String estado) throws SQLException {
        String sql = "UPDATE asistencia SET hora_salida = ?, estado = ? WHERE id_asistencia = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setTime(1, Time.valueOf(horaSalida));
            ps.setString(2, estado);
            ps.setInt(3, idAsistencia);
            ps.executeUpdate();
        }
    }

    // Obtener todas las asistencias
    public List<Asistencia> obtenerTodas() throws SQLException {
        List<Asistencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM asistencia ORDER BY fecha DESC, hora_entrada DESC";
        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearAsistencia(rs));
            }
        }
        return lista;
    }

    // Obtener asistencias por empleado
    public List<Asistencia> obtenerPorEmpleado(int idEmpleado) throws SQLException {
        List<Asistencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM asistencia WHERE id_empleado = ? ORDER BY fecha DESC";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearAsistencia(rs));
                }
            }
        }
        return lista;
    }

    // Obtener actividad reciente (limite de registros)
    public List<Asistencia> obtenerActividadReciente(int limite) throws SQLException {
        List<Asistencia> lista = new ArrayList<>();
        String sql = """
            SELECT * FROM asistencia
            ORDER BY fecha DESC, hora_entrada DESC
            LIMIT ?
        """;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearAsistencia(rs));
                }
            }
        }
        return lista;
    }

    // Mapear ResultSet a objeto Asistencia
    private Asistencia mapearAsistencia(ResultSet rs) throws SQLException {
        java.sql.Time entrada = rs.getTime("hora_entrada");
        java.sql.Time salida = rs.getTime("hora_salida");

        return new Asistencia(
                rs.getInt("id_asistencia"),
                rs.getInt("id_empleado"),
                rs.getDate("fecha") != null ? rs.getDate("fecha").toLocalDate() : null,
                entrada != null ? entrada.toLocalTime() : null,
                salida != null ? salida.toLocalTime() : null,
                rs.getString("estado")
        );
    }

    private List<String[]> obtenerAsistenciaMensual() {
        List<String[]> datos = new ArrayList<>();
        String sql = "SELECT MONTH(fecha) AS mes, COUNT(*) AS total_asistencias "
                + "FROM asistencia GROUP BY MONTH(fecha) ORDER BY mes";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    datos.add(new String[]{String.valueOf(rs.getInt("mes")), String.valueOf(rs.getInt("total_asistencias"))});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datos;
    }

}
