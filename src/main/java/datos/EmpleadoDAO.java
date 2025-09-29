package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import clases.Empleado;
import java.util.LinkedHashMap;
import java.util.Map;

public class EmpleadoDAO {

    private Connection conexion;

    public EmpleadoDAO(Connection conexion) {
        this.conexion = conexion;
    }

    // Método para crear (insertar) un nuevo empleado
    public void crearEmpleado(Empleado empleado) throws SQLException {
        String sql = "INSERT INTO Empleado (nombres, apellidos, dni, fecha_contratacion, correo_electronico, telefono, id_departamento) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, empleado.getNombres());
            ps.setString(2, empleado.getApellidos());
            ps.setString(3, empleado.getDni());
            ps.setDate(4, empleado.getFechaContratacion());
            ps.setString(5, empleado.getCorreoElectronico());
            ps.setString(6, empleado.getTelefono());
            ps.setInt(7, empleado.getIdDepartamento());
            ps.executeUpdate();
        }
    }

    // Método para leer (obtener) todos los empleados
    public List<Empleado> obtenerTodosEmpleados() throws SQLException {
        List<Empleado> listaEmpleados = new ArrayList<>();
        String sql = "SELECT * FROM Empleado ORDER BY id_empleado DESC";
        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                listaEmpleados.add(new Empleado(
                        rs.getInt("id_empleado"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("dni"),
                        rs.getDate("fecha_contratacion"),
                        rs.getString("correo_electronico"),
                        rs.getString("telefono"),
                        rs.getInt("id_departamento"),
                        rs.getBoolean("estado")
                ));
            }
        }
        return listaEmpleados;
    }

    // Método para leer (obtener) un empleado por su ID
    public Empleado obtenerEmpleadoPorId(int id_empleado) throws SQLException {
        String sql = "SELECT * FROM Empleado WHERE id_empleado = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id_empleado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Empleado(
                            rs.getInt("id_empleado"),
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getString("dni"),
                            rs.getDate("fecha_contratacion"),
                            rs.getString("correo_electronico"),
                            rs.getString("telefono"),
                            rs.getInt("id_departamento"),
                            rs.getBoolean("estado")
                    );
                }
            }
        }
        return null; // Retorna null si no se encuentra el empleado
    }

    // Método para actualizar un empleado
    public void actualizarEmpleado(Empleado empleado) throws SQLException {
        String sql = "UPDATE Empleado SET nombres = ?, apellidos = ?, dni = ?, fecha_contratacion = ?, correo_electronico = ?, telefono = ?, id_departamento = ?, estado = ? WHERE id_empleado = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, empleado.getNombres());
            ps.setString(2, empleado.getApellidos());
            ps.setString(3, empleado.getDni());
            ps.setDate(4, empleado.getFechaContratacion());
            ps.setString(5, empleado.getCorreoElectronico());
            ps.setString(6, empleado.getTelefono());
            ps.setInt(7, empleado.getIdDepartamento());
            ps.setBoolean(8, empleado.isEstado());
            ps.setInt(9, empleado.getIdEmpleado());
            ps.executeUpdate();
        }
    }

    // Método para eliminar un empleado
    public void eliminarEmpleado(int id_empleado) throws SQLException {
        String sql = "DELETE FROM Empleado WHERE id_empleado = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id_empleado);
            ps.executeUpdate();
        }
    }

    public int obtenerTotalEmpleados() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Empleado";
        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1); // O rs.getInt("count")
            }
        }
        return 0;
    }

    // Método para contar empleados presentes hoy
    public int obtenerTotalEmpleadosPresentesHoy() throws SQLException {
        String sql = "SELECT COUNT(*) FROM asistencia WHERE fecha = CURRENT_DATE AND estado = 'PRESENTE'";
        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Método para obtener horas trabajadas hoy (total de todos los empleados)
    public double obtenerHorasTotalesHoy() throws SQLException {
        String sql = """
                     SELECT SUM(EXTRACT(EPOCH FROM (a.hora_salida - a.hora_entrada)) / 3600) AS horas_totales
                     FROM asistencia a 
                     WHERE a.fecha = CURRENT_DATE 
                     AND a.hora_entrada IS NOT NULL 
                     AND a.hora_salida IS NOT NULL
                     """;

        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("horas_totales"); // total de horas
            }
        }
        return 0.0; // Si no hay registros
    }

    public int obtenerTotalAusentesHoy() throws SQLException {
        String sql = """
                     SELECT COUNT(*) AS total_ausentes 
                     FROM empleado e 
                     WHERE e.id_empleado NOT IN (SELECT a.id_empleado FROM asistencia a WHERE a.fecha = CURRENT_DATE)
                     """;
        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total_ausentes");
            }
        }
        return 0;
    }

    public double obtenerPromedioHorasHoy() throws SQLException {
        String sql = """
                     SELECT AVG(EXTRACT(EPOCH FROM (a.hora_salida - a.hora_entrada)) / 3600) AS promedio_horas
                     FROM asistencia a
                     WHERE a.fecha = CURRENT_DATE
                     AND a.hora_entrada IS NOT NULL
                     AND a.hora_salida IS NOT NULL
                     """;

        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("promedio_horas");
            }
        }
        return 0.0;
    }

    // Dentro de EmpleadoDAO o AsistenciaDAO
    public List<String> obtenerActividadReciente(int limite) throws SQLException {
        List<String> resultados = new ArrayList<>();

        String sql = """
        SELECT 
            a.id_asistencia,
            e.nombres || ' ' || e.apellidos AS empleado,
            a.fecha,
            a.hora_entrada,
            a.hora_salida,
            CASE 
                WHEN a.hora_salida IS NULL THEN 'En oficina'
                ELSE 'Finalizado'
            END AS estado
        FROM asistencia a
        JOIN empleado e ON a.id_empleado = e.id_empleado
        ORDER BY a.fecha DESC, a.hora_entrada DESC
        LIMIT ?
        """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, limite);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String fila = String.format(
                            "[%s] %s - Entrada: %s | Salida: %s | Estado: %s",
                            rs.getDate("fecha"),
                            rs.getString("empleado"),
                            rs.getTime("hora_entrada"),
                            rs.getTime("hora_salida") != null ? rs.getTime("hora_salida").toString() : "N/A",
                            rs.getString("estado")
                    );
                    resultados.add(fila);
                }
            }
        }

        return resultados;
    }

    public Map<String, Integer> obtenerDistribucionPorDepartamento() throws SQLException {
        String sql = """
        SELECT d.nombre_departamento AS departamento, COUNT(e.id_empleado) AS cantidad
        FROM Departamento d
        LEFT JOIN Empleado e ON d.id_departamento = e.id_departamento
        GROUP BY d.nombre_departamento
        ORDER BY cantidad DESC
            """;

        Map<String, Integer> distribucion = new LinkedHashMap<>(); // Mantiene el orden
        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String departamento = rs.getString("departamento");
                int cantidad = rs.getInt("cantidad");
                distribucion.put(departamento, cantidad);
            }
        }
        return distribucion;
    }

    public Map<String, Integer> obtenerAsistenciaMensual() throws SQLException {
        Map<String, Integer> asistencia = new LinkedHashMap<>();
        String sql = "SELECT TO_CHAR(fecha, 'MM') AS mes, COUNT(*) AS cantidad_asistencias "
                + "FROM asistencia GROUP BY TO_CHAR(fecha, 'MM') ORDER BY mes";

        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String mes = rs.getString("mes");
                int cantidad = rs.getInt("cantidad_asistencias");
                asistencia.put(mes, cantidad);
            }
        }
        return asistencia;
    }

}
