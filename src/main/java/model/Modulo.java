package model;

public class Modulo {

    private int idModulo;
    private String nombreModulo;
    private boolean activo;

    // Constructor vacío
    public Modulo() {}

    // Constructor con parámetros
    public Modulo(int idModulo, String nombreModulo, boolean activo) {
        this.idModulo = idModulo;
        this.nombreModulo = nombreModulo;
        this.activo = activo;
    }

    // Getters
    public int getIdModulo() {
        return idModulo;
    }

    public String getNombreModulo() {
        return nombreModulo;
    }

    public boolean isActivo() {
        return activo;
    }

    // Setters
    public void setIdModulo(int idModulo) {
        this.idModulo = idModulo;
    }

    public void setNombreModulo(String nombreModulo) {
        this.nombreModulo = nombreModulo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return nombreModulo + (activo ? " ✅" : " ❌");
    }
}
