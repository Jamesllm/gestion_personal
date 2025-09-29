/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidades;

import java.io.File;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class GeneradorReporte {

    private Connection conexion;

    public GeneradorReporte(Connection conexion) {
        this.conexion = conexion;
    }

    public void generarReporte(String categoria) {
        try {
            // Ruta del archivo JRXML
            String reportPath = "src/main/resources/reportes/" + categoria + ".jrxml";

            // Compilar el archivo .jrxml
            JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);

            // Parámetros dinámicos
            Map<String, Object> params = new HashMap<>();
            params.put("TITULO", "Reporte de " + categoria);

            JasperPrint print;

            // 🔹 Si el jrxml ya tiene <queryString>, solo le pasamos la conexión
            print = JasperFillManager.fillReport(jasperReport, params, conexion);

            // Crear carpeta si no existe
            File directorio = new File("reportes");
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            // Exportar a PDF
            String outputPath = "reportes/" + categoria + "_reporte.pdf";
            JasperExportManager.exportReportToPdfFile(print, outputPath);

            // Mostrar visor gráfico
            JasperViewer.viewReport(print, false);

            System.out.println("✅ Reporte generado con éxito: " + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
