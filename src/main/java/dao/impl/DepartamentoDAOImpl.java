package dao.impl;

import dao.interfaces.IDepartamentoDAO;
import model.Departamento;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartamentoDAOImpl implements IDepartamentoDAO {

    private static final Logger logger = LoggerFactory.getLogger(DepartamentoDAOImpl.class);
    private final Connection conexion;

    public DepartamentoDAOImpl(Connection conexion) {
        this.conexion = Preconditions.checkNotNull(conexion, "La conexión no puede ser nula");
    }

    @Override
    public void crearDepartamento(Departamento departamento) throws SQLException {
        Preconditions.checkNotNull(departamento, "El departamento no puede ser nulo");
        String sql = "INSERT INTO Departamento (nombre_departamento, estado) VALUES (?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, departamento.getNombreDepartamento());
            ps.setBoolean(2, departamento.isEstado());
            ps.executeUpdate();
            logger.info("Departamento '{}' creado correctamente.", departamento.getNombreDepartamento());
        } catch (SQLException e) {
            logger.error("Error al crear el departamento '{}': {}", departamento.getNombreDepartamento(), e.getMessage(), e);
            throw e;
        }
    }

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

            logger.info("Se obtuvieron {} departamentos del sistema.", listaDepartamentos.size());
        } catch (SQLException e) {
            logger.error("Error al obtener la lista de departamentos: {}", e.getMessage(), e);
            throw e;
        }
        return listaDepartamentos.isEmpty() ? ImmutableList.of() : listaDepartamentos;
    }

    @Override
    public Departamento obtenerDepartamentoPorId(int id_departamento) throws SQLException {
        Preconditions.checkArgument(id_departamento > 0, "El ID debe ser mayor que 0");
        String sql = "SELECT * FROM Departamento WHERE id_departamento = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id_departamento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    logger.info("Departamento con ID {} obtenido correctamente.", id_departamento);
                    return new Departamento(
                            rs.getInt("id_departamento"),
                            rs.getString("nombre_departamento"),
                            rs.getBoolean("estado")
                    );
                } else {
                    logger.warn("No se encontró ningún departamento con ID {}", id_departamento);
                }
            }
        } catch (SQLException e) {
            logger.error("Error al obtener el departamento con ID {}: {}", id_departamento, e.getMessage(), e);
            throw e;
        }
        return null;
    }

    @Override
    public void actualizarDepartamento(Departamento departamento) throws SQLException {
        Preconditions.checkNotNull(departamento, "El departamento no puede ser nulo");
        String sql = "UPDATE Departamento SET nombre_departamento = ?, estado = ? WHERE id_departamento = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, departamento.getNombreDepartamento());
            ps.setBoolean(2, departamento.isEstado());
            ps.setInt(3, departamento.getIdDepartamento());
            ps.executeUpdate();
            logger.info("Departamento '{}' actualizado correctamente.", departamento.getNombreDepartamento());
        } catch (SQLException e) {
            logger.error("Error al actualizar el departamento '{}': {}", departamento.getNombreDepartamento(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void eliminarDepartamento(int id_departamento) throws SQLException {
        Preconditions.checkArgument(id_departamento > 0, "El ID debe ser mayor que 0");
        String sql = "DELETE FROM Departamento WHERE id_departamento = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id_departamento);
            ps.executeUpdate();
            logger.info("Departamento con ID {} eliminado correctamente.", id_departamento);
        } catch (SQLException e) {
            logger.error("Error al eliminar el departamento con ID {}: {}", id_departamento, e.getMessage(), e);
            throw e;
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
                datos.add(new String[]{
                    rs.getString("departamento"),
                    String.valueOf(rs.getInt("cantidad_empleados"))
                });
            }

            logger.info("Se generó la distribución de empleados por departamento. Total: {}", datos.size());
        } catch (SQLException e) {
            logger.error("Error al obtener la distribución de departamentos: {}", e.getMessage(), e);
        }
        return datos.isEmpty() ? ImmutableList.of() : datos;
    }
}
