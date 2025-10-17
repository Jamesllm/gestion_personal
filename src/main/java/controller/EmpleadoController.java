package controller;

import dao.impl.EmpleadoDAOImpl;
import dao.impl.Conexion;
import model.Empleado;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;

public class EmpleadoController {

    private EmpleadoDAOImpl empleadoDAO;

    public EmpleadoController(Conexion conexion) {
        this.empleadoDAO = new EmpleadoDAOImpl(conexion.getConexion());
    }

    public boolean exportarEmpleadosAExcel(String rutaArchivo) {
        List<Empleado> empleados = obtenerEmpleados();
        if (empleados.isEmpty()) {
            System.out.println("⚠ No hay empleados para exportar.");
            return false;
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Empleados");

            // === Cabecera ===
            Row header = sheet.createRow(0);
            String[] columnas = {"ID", "Nombres", "Apellidos", "DNI", "Fecha Contratación", "Correo", "Teléfono", "Departamento", "Estado"};
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columnas[i]);
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            // === Datos ===
            int fila = 1;
            for (Empleado e : empleados) {
                Row row = sheet.createRow(fila++);
                row.createCell(0).setCellValue(e.getIdEmpleado());
                row.createCell(1).setCellValue(e.getNombres());
                row.createCell(2).setCellValue(e.getApellidos());
                row.createCell(3).setCellValue(e.getDni());
                row.createCell(4).setCellValue(e.getFechaContratacion().toString());
                row.createCell(5).setCellValue(e.getCorreoElectronico());
                row.createCell(6).setCellValue(e.getTelefono());
                row.createCell(7).setCellValue(e.getIdDepartamento());
                row.createCell(8).setCellValue(e.isEstado() ? "Activo" : "Inactivo");
            }

            // Autoajustar columnas
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // === Guardar archivo ===
            try (FileOutputStream fileOut = new FileOutputStream(rutaArchivo)) {
                workbook.write(fileOut);
            }

            System.out.println("✅ Archivo Excel generado correctamente: " + rutaArchivo);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ Error al exportar empleados a Excel: " + e.getMessage());
            return false;
        }
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
