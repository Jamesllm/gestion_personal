package model;

public class Departamento {

    private int idDepartamento;
    private String nombreDepartamento;
    private boolean estado; // Agregado: Campo para el estado del departamento

    // Constructor vacío
    public Departamento() {
    }

    // Constructor con todos los atributos
    public Departamento(int idDepartamento, String nombreDepartamento, boolean estado) {
        this.idDepartamento = idDepartamento;
        this.nombreDepartamento = nombreDepartamento;
        this.estado = estado;
    }

    // Constructor sin id (útil para insertar nuevos departamentos)
    public Departamento(String nombreDepartamento, boolean estado) {
        this.nombreDepartamento = nombreDepartamento;
        this.estado = estado;
    }

    // Getters
    public int getIdDepartamento() {
        return idDepartamento;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public boolean isEstado() {
        return estado;
    }

    // Setters
    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return nombreDepartamento; // se mostrará el nombre en el combo
    }

}
