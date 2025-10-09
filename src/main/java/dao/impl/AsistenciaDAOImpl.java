package dao.impl;

import dao.interfaces.IAsistenciaDAO;
import model.Asistencia;
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

    private final Connection conexion;

    public AsistenciaDAOImpl(Connection conexion) {
        this.conexion = conexion;
    }

    // Insertar nueva asistencia (entrada)
    @Override
    public void registrarEntrada(Asistencia asistencia) throws SQLException {
        String sql = "INSERT INTO asistencia (id_empleado, hora_entrada, hora_salida, estado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, asistencia.getIdEmpleado());
            ps.setTime(2, asistencia.getHoraEntrada());
            ps.setTime(3, asistencia.getHoraSalida());
            ps.setString(4, asistencia.getEstado());
            ps.executeUpdate();
        }
    }

    // Registrar salida
    @Override
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
    @Override
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

    @Override
    public List<Asistencia> obtenerTodasUsuario(int idEmpleado) throws SQLException {
        List<Asistencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM asistencia ORDER BY fecha DESC, hora_entrada DESC WHERE id_empleado = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            ps.setInt(1, idEmpleado);
            while (rs.next()) {
                lista.add(mapearAsistencia(rs));
            }
        }
        return lista;
    }

    @Override
    public int obtenerAsistenciaActiva(int idEmpleado) throws SQLException {
        String sql = "SELECT id_asistencia FROM asistencia WHERE id_empleado = ? AND hora_salida IS NULL";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_asistencia");
                }
            }
        }
        return -1; // No se encontró asistencia activa
    }

    // Obtener asistencias por empleado
    @Override
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
    @Override
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
    @Override
    public Asistencia mapearAsistencia(ResultSet rs) throws SQLException {
        java.sql.Date fechaSql = rs.getDate("fecha");
        java.sql.Time entradaSql = rs.getTime("hora_entrada");
        java.sql.Time salidaSql = rs.getTime("hora_salida");

        return new Asistencia(
                rs.getInt("id_asistencia"),
                rs.getInt("id_empleado"),
                fechaSql, // si tu constructor espera java.sql.Date
                entradaSql, // si tu constructor espera java.sql.Time
                salidaSql, // igual aquí
                rs.getString("estado")
        );
    }

    @Override
    public List<String[]> obtenerAsistenciaMensual() {
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
