/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author James
 */
import java.sql.Timestamp;

public class Inventario {

    private int idItem;
    private String nombreItem;
    private int stockActual;
    private String unidad;
    private String ubicacion;
    private Timestamp fechaUltimoMovimiento;
    private int stockMinimo;
    private double precioUnitario;

    public Inventario() {
    }
    
    

    public Inventario(String nombreItem, int stockActual, String unidad, String ubicacion, int stockMinimo, double precioUnitario) {
        this.nombreItem = nombreItem;
        this.stockActual = stockActual;
        this.unidad = unidad;
        this.ubicacion = ubicacion;
        this.stockMinimo = stockMinimo;
        this.precioUnitario = precioUnitario;
    }

    // Getters y Setters
    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public String getNombreItem() {
        return nombreItem;
    }

    public void setNombreItem(String nombreItem) {
        this.nombreItem = nombreItem;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Timestamp getFechaUltimoMovimiento() {
        return fechaUltimoMovimiento;
    }

    public void setFechaUltimoMovimiento(Timestamp fechaUltimoMovimiento) {
        this.fechaUltimoMovimiento = fechaUltimoMovimiento;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}
