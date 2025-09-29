/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author James
 */
import java.time.LocalDate; // Para manejar fechas
import java.time.LocalTime; // Para manejar horas

public class Asistencia {

    private int idAsistencia;
    private int idEmpleado; // Clave foránea
    private LocalDate fecha;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private String estado; // Ej: "A tiempo", "Tardanza", "Ausente", "Permiso"

    // Constructor vacío
    public Asistencia() {
    }

    // Constructor con todos los atributos (sin idAsistencia, para inserción)
    public Asistencia(int idEmpleado, LocalDate fecha, LocalTime horaEntrada, LocalTime horaSalida, String estado) {
        this.idEmpleado = idEmpleado;
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.estado = estado;
    }

    // Constructor con todos los atributos (incluyendo idAsistencia)
    public Asistencia(int idAsistencia, int idEmpleado, LocalDate fecha, LocalTime horaEntrada, LocalTime horaSalida, String estado) {
        this.idAsistencia = idAsistencia;
        this.idEmpleado = idEmpleado;
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.estado = estado;
    }

    // Constructor para registro de entrada (sin hora de salida)
    public Asistencia(int idEmpleado, LocalDate fecha, LocalTime horaEntrada, String estado) {
        this.idEmpleado = idEmpleado;
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.estado = estado;
    }

    // Getters
    public int getIdAsistencia() {
        return idAsistencia;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHoraEntrada() {
        return horaEntrada;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public String getEstado() {
        return estado;
    }

    // Setters
    public void setIdAsistencia(int idAsistencia) {
        this.idAsistencia = idAsistencia;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setHoraEntrada(LocalTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // toString
    @Override
    public String toString() {
        return "Asistencia{"
                + "idAsistencia=" + idAsistencia
                + ", idEmpleado=" + idEmpleado
                + ", fecha=" + fecha
                + ", horaEntrada=" + horaEntrada
                + ", horaSalida=" + horaSalida
                + ", estado='" + estado + '\''
                + '}';
    }
}
