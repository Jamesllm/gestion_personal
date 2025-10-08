package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

public class GeneradorReporte {
    private static final Logger LOGGER = Logger.getLogger(GeneradorReporte.class.getName());
    private Connection conexion;

    public GeneradorReporte(Connection conexion) {
        this.conexion = conexion;
    }

    public void generarReporte(String categoria, String titulo) {
        try {
            // Validar categoría
            if (categoria == null || categoria.trim().isEmpty()) {
                throw new IllegalArgumentException("La categoría no puede estar vacía");
            }

            // Ruta del archivo JRXML
            String reportPath = "src/main/resources/reportes/" + categoria + ".jrxml";
            File reportFile = new File(reportPath);
            
            if (!reportFile.exists()) {
                throw new FileNotFoundException("No se encontró el archivo de reporte: " + reportPath);
            }

            // Compilar el archivo .jrxml
            LOGGER.info("Compilando reporte: " + reportPath);
            JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);

            // Parámetros dinámicos
            Map<String, Object> params = new HashMap<>();
            params.put("TITULO", "Reporte de " + titulo);

            // Parámetros de la empresa
            String logoPath = "src/main/resources/imagenes/logo.png";
            File logoFile = new File(logoPath);
            
            params.put("NOMBRE_EMPRESA", "MODAS TEXTILES DRAGO S.A.C.");
            params.put("RUC_EMPRESA", "20123456789");
            params.put("DIRECCION_EMPRESA", "Av. Principal 123, Lima - Perú");
            params.put("TELEFONO_EMPRESA", "(01) 234-5678");
            params.put("EMAIL_EMPRESA", "contacto@miempresa.com");

            // Llenar el reporte con datos
            LOGGER.info("Generando reporte...");
            JasperPrint print = JasperFillManager.fillReport(jasperReport, params, conexion);

            // Crear carpeta si no existe
            File directorio = new File("reportes");
            if (!directorio.exists()) {
                boolean creado = directorio.mkdirs();
                if (creado) {
                    LOGGER.info("Directorio 'reportes' creado exitosamente");
                }
            }

            // Exportar a PDF
            String outputPath = "reportes/" + categoria + "_reporte.pdf";
            JasperExportManager.exportReportToPdfFile(print, outputPath);
            LOGGER.info("Reporte generado con éxito: " + outputPath);

            // Mostrar visor gráfico
            JasperViewer.viewReport(print, false);

        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Archivo no encontrado: " + e.getMessage(), e);
            throw new RuntimeException("Error: Archivo de reporte no encontrado", e);
        } catch (JRException e) {
            LOGGER.log(Level.SEVERE, "Error al generar el reporte JasperReports", e);
            throw new RuntimeException("Error al procesar el reporte: " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al generar reporte", e);
            throw new RuntimeException("Error inesperado: " + e.getMessage(), e);
        }
    }

    /**
     * Genera un reporte sin mostrar el visor (útil para generación batch)
     */
    public String generarReporteSilencioso(String categoria, String titulo) {
        try {
            String reportPath = "src/main/resources/reportes/" + categoria + ".jrxml";
            JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);

            Map<String, Object> params = new HashMap<>();
            params.put("TITULO", "Reporte de " + titulo);
            params.put("LOGO_EMPRESA", "src/main/resources/imagenes/logo.png");
            params.put("NOMBRE_EMPRESA", "MODAS TEXTILES DRAGO S.A.C.");
            params.put("RUC_EMPRESA", "20123456789");
            params.put("DIRECCION_EMPRESA", "Av. Principal 123, Lima - Perú");
            params.put("TELEFONO_EMPRESA", "(01) 234-5678");
            params.put("EMAIL_EMPRESA", "contacto@miempresa.com");

            JasperPrint print = JasperFillManager.fillReport(jasperReport, params, conexion);

            File directorio = new File("reportes");
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            String outputPath = "reportes/" + categoria + "_reporte.pdf";
            JasperExportManager.exportReportToPdfFile(print, outputPath);
            
            return outputPath;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al generar reporte silencioso", e);
            return null;
        }
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }
}