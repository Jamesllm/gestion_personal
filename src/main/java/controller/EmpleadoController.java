package controller;

import dao.impl.EmpleadoDAOImpl;
import dao.impl.Conexion;
import model.Empleado;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class EmpleadoController {

    private EmpleadoDAOImpl empleadoDAO;

    public EmpleadoController(Conexion conexion) {
        this.empleadoDAO = new EmpleadoDAOImpl(conexion.getConexion());
    }

    // === CRUD ===
    public void crearEmpleado(Empleado empleado) {
        try {
            empleadoDAO.crearEmpleado(empleado);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear el empleado: " + e.getMessage());
        }
    }

    public List<Empleado> obtenerEmpleados() {
        try {
            return empleadoDAO.obtenerTodosEmpleados();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener empleados: " + e.getMessage());
        }
    }

    public Empleado obtenerEmpleadoPorId(int id) {
        try {
            return empleadoDAO.obtenerEmpleadoPorId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener el empleado: " + e.getMessage());
        }
    }
    
    public int obtenerEmpleadoPorDNI(String id) {
        try {
            return empleadoDAO.obtenerIdPorDni(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener el empleado: " + e.getMessage());
        }
    }

    public void actualizarEmpleado(Empleado empleado) {
        try {
            empleadoDAO.actualizarEmpleado(empleado);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el empleado: " + e.getMessage());
        }
    }

    public void eliminarEmpleado(int idEmpleado) {
        try {
            empleadoDAO.eliminarEmpleado(idEmpleado);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar el empleado: " + e.getMessage());
        }
    }

    // === REPORTES Y MÉTRICAS ===
    public int contarTotalEmpleados() {
        try {
            return empleadoDAO.obtenerTotalEmpleados();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int contarPresentesHoy() {
        try {
            return empleadoDAO.obtenerTotalEmpleadosPresentesHoy();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int contarAusentesHoy() {
        try {
            return empleadoDAO.obtenerTotalAusentesHoy();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public double obtenerHorasTotalesHoy() {
        try {
            return empleadoDAO.obtenerHorasTotalesHoy();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public double obtenerPromedioHorasHoy() {
        try {
            return empleadoDAO.obtenerPromedioHorasHoy();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public List<String> obtenerActividadReciente(int limite) {
        try {
            return empleadoDAO.obtenerActividadReciente(limite);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener actividad reciente: " + e.getMessage());
        }
    }

    public Map<String, Integer> obtenerDistribucionPorDepartamento() {
        try {
            return empleadoDAO.obtenerDistribucionPorDepartamento();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener distribución por departamento: " + e.getMessage());
        }
    }

    public Map<String, Integer> obtenerAsistenciaMensual() {
        try {
            return empleadoDAO.obtenerAsistenciaMensual();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener asistencia mensual: " + e.getMessage());
        }
    }
}
