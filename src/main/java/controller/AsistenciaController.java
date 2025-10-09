package controller;

import dao.impl.AsistenciaDAOImpl;
import dao.impl.Conexion;
import model.Asistencia;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

public class AsistenciaController {

    private AsistenciaDAOImpl asistenciaDAO;

    public AsistenciaController(Conexion conexion) {
        this.asistenciaDAO = new AsistenciaDAOImpl(conexion.getConexion());
    }

    // === REGISTROS DE ASISTENCIA ===
    public void registrarEntrada(Asistencia asistencia) {
        try {
            asistenciaDAO.registrarEntrada(asistencia);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al registrar la entrada: " + e.getMessage());
        }
    }

    public void registrarSalida(int idAsistencia, LocalTime horaSalida, String estado) {
        try {
            asistenciaDAO.registrarSalida(idAsistencia, horaSalida, estado);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al registrar la salida: " + e.getMessage());
        }
    }

    // === CONSULTAS GENERALES ===
    public List<Asistencia> obtenerTodas() {
        try {
            return asistenciaDAO.obtenerTodas();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener todas las asistencias: " + e.getMessage());
        }
    }

    public List<Asistencia> obtenerPorEmpleado(int idEmpleado) {
        try {
            return asistenciaDAO.obtenerPorEmpleado(idEmpleado);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener asistencias del empleado: " + e.getMessage());
        }
    }

    public List<Asistencia> obtenerTodasUsuario(int idEmpleado) {
        try {
            return asistenciaDAO.obtenerTodasUsuario(idEmpleado);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener asistencias del usuario: " + e.getMessage());
        }
    }

    // === REPORTES Y ESTADÍSTICAS ===
    public List<Asistencia> obtenerActividadReciente(int limite) {
        try {
            return asistenciaDAO.obtenerActividadReciente(limite);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener la actividad reciente: " + e.getMessage());
        }
    }

    public List<String[]> obtenerAsistenciaMensual() {
        try {
            return asistenciaDAO.obtenerAsistenciaMensual();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener la asistencia mensual: " + e.getMessage());
        }
    }

    // === UTILIDAD ===
    public int obtenerAsistenciaActiva(int idEmpleado) {
        try {
            return asistenciaDAO.obtenerAsistenciaActiva(idEmpleado);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al verificar asistencia activa: " + e.getMessage());
        }
    }
}
