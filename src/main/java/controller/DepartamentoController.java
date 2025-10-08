package controller;

import dao.impl.DepartamentoDAOImpl;
import dao.impl.Conexion;
import model.Departamento;

import java.sql.SQLException;
import java.util.List;

public class DepartamentoController {

    private DepartamentoDAOImpl departamentoDAO;

    public DepartamentoController(Conexion conexion) {
        this.departamentoDAO = new DepartamentoDAOImpl(conexion.getConexion());
    }

    // === CRUD ===
    public void crearDepartamento(Departamento departamento) {
        try {
            departamentoDAO.crearDepartamento(departamento);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear el departamento: " + e.getMessage());
        }
    }

    public List<Departamento> obtenerDepartamentos() {
        try {
            return departamentoDAO.obtenerTodosDepartamentos();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener departamentos: " + e.getMessage());
        }
    }

    public Departamento obtenerDepartamentoPorId(int idDepartamento) {
        try {
            return departamentoDAO.obtenerDepartamentoPorId(idDepartamento);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener el departamento: " + e.getMessage());
        }
    }

    public void actualizarDepartamento(Departamento departamento) {
        try {
            departamentoDAO.actualizarDepartamento(departamento);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el departamento: " + e.getMessage());
        }
    }

    public void eliminarDepartamento(int idDepartamento) {
        try {
            departamentoDAO.eliminarDepartamento(idDepartamento);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar el departamento: " + e.getMessage());
        }
    }

    // === REPORTES Y MÉTRICAS ===
    public List<String[]> obtenerDistribucionDepartamentos() {
        try {
            return departamentoDAO.obtenerDistribucionDepartamentos();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener la distribución de departamentos: " + e.getMessage());
        }
    }
}
