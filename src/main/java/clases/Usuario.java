/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author James
 */
public class Usuario {

    private int idUsuario;
    private String nombreUsuario;
    private String contrasena;
    private int idRol; // Clave foránea
    private int idEmpleado; // Clave foránea (y única)
    private Rol rol; // Relación con Rol

    // Constructor vacío
    public Usuario() {
    }

    // Constructor con todos los atributos (sin idUsuario, para inserción)
    public Usuario(String nombreUsuario, String contrasena, Rol rol, int idEmpleado) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
        this.idEmpleado = idEmpleado;
    }

    // Constructor con todos los atributos (incluyendo idUsuario)
    public Usuario(int idUsuario, String nombreUsuario, String contrasena, Rol rol, int idEmpleado) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
        this.idEmpleado = idEmpleado;
    }

    // Getters
    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public int getIdRol() {
        return idRol;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public Rol getRol() {
        return rol;
    }

    // Setters
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    // toString
    @Override
    public String toString() {
        return "Usuario{"
                + "idUsuario=" + idUsuario
                + ", nombreUsuario='" + nombreUsuario + '\''
                + ", contrasena='" + contrasena + '\''
                + ", idRol=" + idRol
                + ", idEmpleado=" + idEmpleado
                + '}';
    }
}
