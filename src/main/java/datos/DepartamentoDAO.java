package datos;

import clases.Departamento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartamentoDAO {

    private Connection conexion;

    public DepartamentoDAO(Connection conexion) {
        this.conexion = conexion;
    }

    // Método para crear (insertar) un nuevo departamento
    public void crearDepartamento(Departamento departamento) throws SQLException {
        String sql = "INSERT INTO Departamento (nombre_departamento, estado) VALUES (?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, departamento.getNombreDepartamento());
            ps.setBoolean(2, departamento.isEstado());
            ps.executeUpdate();
        }
    }

    // Método para leer (obtener) todos los departamentos
    public List<Departamento> obtenerTodosDepartamentos() throws SQLException {
        List<Departamento> listaDepartamentos = new ArrayList<>();
        String sql = "SELECT * FROM Departamento";
        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                listaDepartamentos.add(new Departamento(
                        rs.getInt("id_departamento"),
                        rs.getString("nombre_departamento"),
                        rs.getBoolean("estado")
                ));
            }
        }
        return listaDepartamentos;
    }

    // Método para leer (obtener) un departamento por su ID
    public Departamento obtenerDepartamentoPorId(int id_departamento) throws SQLException {
        String sql = "SELECT * FROM Departamento WHERE id_departamento = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id_departamento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Departamento(
                            rs.getInt("id_departamento"),
                            rs.getString("nombre_departamento"),
                            rs.getBoolean("estado")
                    );
                }
            }
        }
        return null;
    }

    // Método para actualizar un departamento
    public void actualizarDepartamento(Departamento departamento) throws SQLException {
        String sql = "UPDATE Departamento SET nombre_departamento = ?, estado = ? WHERE id_departamento = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, departamento.getNombreDepartamento());
            ps.setBoolean(2, departamento.isEstado());
            ps.setInt(3, departamento.getIdDepartamento());
            ps.executeUpdate();
        }
    }

    // Método para eliminar un departamento
    public void eliminarDepartamento(int id_departamento) throws SQLException {
        String sql = "DELETE FROM Departamento WHERE id_departamento = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id_departamento);
            ps.executeUpdate();
        }
    }
}
