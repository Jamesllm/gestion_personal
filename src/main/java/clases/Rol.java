package clases;

import java.util.List;

public class Rol {

    private int idRol;
    private String nombreRol;
    private List<Modulo> modulos; // Relación con módulos

    // Constructor vacío
    public Rol() {
    }

    public Rol(int idRol, String nombreRol) {
        this.idRol = idRol;
        this.nombreRol = nombreRol;
    }

    // Constructor con módulos
    public Rol(int idRol, String nombreRol, List<Modulo> modulos) {
        this.idRol = idRol;
        this.nombreRol = nombreRol;
        this.modulos = modulos;
    }

    // Getters
    public int getIdRol() {
        return idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public List<Modulo> getModulos() {
        return modulos;
    }

    // Setters
    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public void setModulos(List<Modulo> modulos) {
        this.modulos = modulos;
    }

    @Override
    public String toString() {
        return "Rol{"
                + "idRol=" + idRol
                + ", nombreRol='" + nombreRol + '\''
                + ", modulos=" + modulos
                + '}';
    }
}
