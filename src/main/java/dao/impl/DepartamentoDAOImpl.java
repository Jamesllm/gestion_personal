package dao.impl;

import dao.interfaces.IDepartamentoDAO;
import model.Departamento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartamentoDAOImpl implements IDepartamentoDAO {

    private Connection conexion;

    public DepartamentoDAOImpl(Connection conexion) {
        this.conexion = conexion;
    }

    // Método para crear (insertar) un nuevo departamento
    @Override
    public void crearDepartamento(Departamento departamento) throws SQLException {
        String sql = "INSERT INTO Departamento (nombre_departamento, estado) VALUES (?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, departamento.getNombreDepartamento());
            ps.setBoolean(2, departamento.isEstado());
            ps.executeUpdate();
        }
    }

    // Método para leer (obtener) todos los departamentos
    @Override
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
    @Override
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
    @Override
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
    @Override
    public void eliminarDepartamento(int id_departamento) throws SQLException {
        String sql = "DELETE FROM Departamento WHERE id_departamento = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id_departamento);
            ps.executeUpdate();
        }
    }

    @Override
    public List<String[]> obtenerDistribucionDepartamentos() {
        List<String[]> datos = new ArrayList<>();
        String sql = "SELECT d.nombre AS departamento, COUNT(e.id_empleado) AS cantidad_empleados "
                + "FROM empleados e INNER JOIN departamentos d ON e.id_departamento = d.id_departamento "
                + "GROUP BY d.nombre";
        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                datos.add(new String[]{rs.getString("departamento"), String.valueOf(rs.getInt("cantidad_empleados"))});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datos;
    }
}
