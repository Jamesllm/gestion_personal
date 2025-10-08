/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author James
 */
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate; // Para manejar fechas
import java.time.LocalTime; // Para manejar horas

public class Asistencia {

    private int idAsistencia;
    private int idEmpleado; // Clave foránea
    private Date fecha;
    private Time  horaEntrada;
    private Time  horaSalida;
    private String estado; // Ej: "A tiempo", "Tardanza", "Ausente", "Permiso"

    // Constructor vacío
    public Asistencia() {
    }

    // Constructor con todos los atributos (sin idAsistencia, para inserción)
    public Asistencia(int idEmpleado, Date fecha, Time  horaEntrada, Time  horaSalida, String estado) {
        this.idEmpleado = idEmpleado;
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.estado = estado;
    }

    // Constructor con todos los atributos (incluyendo idAsistencia)
    public Asistencia(int idAsistencia, int idEmpleado, Date fecha, Time  horaEntrada, Time  horaSalida, String estado) {
        this.idAsistencia = idAsistencia;
        this.idEmpleado = idEmpleado;
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.estado = estado;
    }

    // Constructor para registro de entrada (sin hora de salida)
    public Asistencia(int idEmpleado, Date fecha, Time  horaEntrada, String estado) {
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

    public Date getFecha() {
        return fecha;
    }

    public Time  getHoraEntrada() {
        return horaEntrada;
    }

    public Time  getHoraSalida() {
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

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setHoraEntrada(Time  horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public void setHoraSalida(Time  horaSalida) {
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
