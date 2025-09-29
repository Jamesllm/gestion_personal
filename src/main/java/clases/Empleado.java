package clases;

import java.sql.Date; // Use java.sql.Date to match JDBC driver

public class Empleado {

    private int idEmpleado;
    private String nombres;
    private String apellidos;
    private String dni;
    private java.sql.Date fechaContratacion;
    private String correoElectronico;
    private String telefono;
    private int idDepartamento;
    private boolean estado; // Add the missing 'estado' field

    // Constructor vacío
    public Empleado() {
    }

    // Constructor with all attributes (without idEmpleado, for insertion)
    public Empleado(String nombres, String apellidos, String dni, java.sql.Date fechaContratacion,
            String correoElectronico, String telefono, int idDepartamento) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.fechaContratacion = fechaContratacion;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
        this.idDepartamento = idDepartamento;
    }

    // Constructor with all attributes (including idEmpleado)
    public Empleado(int idEmpleado, String nombres, String apellidos, String dni, Date fechaContratacion,
            String correoElectronico, String telefono, int idDepartamento, boolean estado) {
        this.idEmpleado = idEmpleado;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.fechaContratacion = fechaContratacion;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
        this.idDepartamento = idDepartamento;
        this.estado = estado;
    }

    // Getters
    public int getIdEmpleado() {
        return idEmpleado;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getDni() {
        return dni;
    }

    public Date getFechaContratacion() {
        return fechaContratacion;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getTelefono() {
        return telefono;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public boolean isEstado() {
        return estado;
    }

    // Setters
    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setFechaContratacion(Date fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    // toString
    @Override
    public String toString() {
        return "Empleado{"
                + "idEmpleado=" + idEmpleado
                + ", nombres='" + nombres + '\''
                + ", apellidos='" + apellidos + '\''
                + ", dni='" + dni + '\''
                + ", fechaContratacion=" + fechaContratacion
                + ", correoElectronico='" + correoElectronico + '\''
                + ", telefono='" + telefono + '\''
                + ", idDepartamento=" + idDepartamento
                + ", estado=" + estado
                + '}';
    }
}
