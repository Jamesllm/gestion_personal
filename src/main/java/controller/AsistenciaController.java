package controller;

import dao.impl.AsistenciaDAOImpl;
import dao.impl.Conexion;
import model.Asistencia;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

public class AsistenciaController {

    private final AsistenciaDAOImpl asistenciaDAO;

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

    // === EXPORTAR A EXCEL ===
    public boolean exportarAsistenciasAExcel(String rutaArchivo) {
        List<Asistencia> asistencias = obtenerTodas();

        if (asistencias.isEmpty()) {
            System.out.println("No hay datos de asistencia para exportar.");
            return false;
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Asistencias");

            // Estilo de cabecera
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Crear cabecera
            Row headerRow = sheet.createRow(0);
            String[] columnas = {"ID", "Empleado", "Fecha", "Hora Entrada", "Hora Salida", "Estado"};
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            // Llenar filas
            int rowNum = 1;
            for (Asistencia a : asistencias) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(a.getIdAsistencia());
                row.createCell(1).setCellValue(a.getIdEmpleado()); // Ajusta según tu modelo
                row.createCell(2).setCellValue(a.getFecha().toString());
                row.createCell(3).setCellValue(a.getHoraEntrada() != null ? a.getHoraEntrada().toString() : "");
                row.createCell(4).setCellValue(a.getHoraSalida() != null ? a.getHoraSalida().toString() : "");
                row.createCell(5).setCellValue(a.getEstado());
            }

            // Autoajustar columnas
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Guardar archivo
            try (FileOutputStream fileOut = new FileOutputStream(rutaArchivo)) {
                workbook.write(fileOut);
            }

            System.out.println("✅ Reporte exportado correctamente a: " + rutaArchivo);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ Error al exportar el archivo: " + e.getMessage());
            return false;
        }
    }

}
