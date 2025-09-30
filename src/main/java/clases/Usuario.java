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
    private String username;
    private String password;
    private boolean cambiarPassword; // para forzar cambio en el primer ingreso
    private Rol rol; // Asociación a la clase Rol
    private int idEmpleado; // vínculo con empleado si corresponde

    // Constructor vacío
    public Usuario() {
    }

    // Constructor con parámetros
    public Usuario(int idUsuario, String username, String password, Rol rol, int idEmpleado) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.idEmpleado = idEmpleado;
    }
    
    public Usuario(int idUsuario, String username, String password, boolean cambiarPassword, Rol rol, int idEmpleado) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.password = password;
        this.cambiarPassword = cambiarPassword;
        this.rol = rol;
        this.idEmpleado = idEmpleado;
    }

    // Getters y Setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isCambiarPassword() {
        return cambiarPassword;
    }

    public void setCambiarPassword(boolean cambiarPassword) {
        this.cambiarPassword = cambiarPassword;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    @Override
    public String toString() {
        return "Usuario{"
                + "idUsuario=" + idUsuario
                + ", username='" + username + '\''
                + ", rol=" + (rol != null ? rol.getNombreRol() : "sin rol")
                + '}';
    }
}
