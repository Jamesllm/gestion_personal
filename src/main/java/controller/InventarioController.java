package controller;

import dao.impl.Conexion;
import dao.impl.InventarioDAOImpl;
import dao.interfaces.IInventarioDAO;
import model.Inventario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador para la gestión del Inventario.
 * Actúa como intermediario entre la vista y la capa DAO.
 *
 * Proporciona métodos CRUD para manejar los items del inventario.
 * 
 * @author James
 */
public class InventarioController {

    private static final Logger LOGGER = Logger.getLogger(InventarioController.class.getName());
    private final IInventarioDAO inventarioDAO;

    /**
     * Constructor del controlador.
     *
     * @param conexion conexión activa a la base de datos.
     */
    public InventarioController(Conexion conexion) {
        this.inventarioDAO = new InventarioDAOImpl(conexion.getConexion());
    }

    /**
     * Inserta un nuevo item en el inventario.
     *
     * @param inventario objeto Inventario con los datos a registrar.
     * @return true si se insertó correctamente, false si ocurrió un error.
     */
    public boolean insertar(Inventario inventario) {
        try {
            return inventarioDAO.insertar(inventario);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al insertar item en inventario", ex);
            return false;
        }
    }

    /**
     * Actualiza un item existente.
     *
     * @param inventario objeto Inventario con los nuevos datos.
     * @return true si se actualizó correctamente, false si ocurrió un error.
     */
    public boolean actualizar(Inventario inventario) {
        try {
            return inventarioDAO.actualizar(inventario);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al actualizar item del inventario", ex);
            return false;
        }
    }

    /**
     * Elimina (desactiva) un item del inventario.
     *
     * @param idItem ID del item a eliminar.
     * @return true si se eliminó correctamente, false si ocurrió un error.
     */
    public boolean eliminar(int idItem) {
        try {
            return inventarioDAO.eliminar(idItem);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al eliminar item del inventario", ex);
            return false;
        }
    }

    /**
     * Busca un item por su ID.
     *
     * @param idItem ID del item a buscar.
     * @return el objeto Inventario si existe, o null si no se encontró.
     */
    public Inventario buscarPorId(int idItem) {
        try {
            return inventarioDAO.buscarPorId(idItem);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar item en inventario con ID: " + idItem, ex);
            return null;
        }
    }

    /**
     * Lista todos los items del inventario.
     *
     * @return lista de objetos Inventario o lista vacía si ocurre un error.
     */
    public List<Inventario> listar() {
        try {
            return inventarioDAO.listar();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al listar los items del inventario", ex);
            return List.of();
        }
    }
}
