package model;
import java.util.Objects;

public class Usuario {

    private int idUsuario;
    private String username;
    private String password;
    private boolean cambiarPassword; 
    private Rol rol;
    private int idEmpleado; 

    // Constructores
    public Usuario() {}

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

    // 👇 Implementación correcta de equals() y hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return idUsuario == usuario.idUsuario &&
               Objects.equals(username, usuario.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, username);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", username='" + username + '\'' +
                ", rol=" + (rol != null ? rol.getNombreRol() : "sin rol") +
                '}';
    }
}
