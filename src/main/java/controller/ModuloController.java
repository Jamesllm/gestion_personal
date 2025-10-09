package controller;

import dao.impl.Conexion;
import dao.impl.ModuloDAOImpl;
import dao.interfaces.IModuloDAO;
import model.Modulo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador para la gestión de módulos del sistema.
 * <p>
 * Actúa como intermediario entre la interfaz de usuario y la capa DAO,
 * permitiendo obtener los módulos asociados a un rol específico.
 * </p>
 * 
 * @author James
 */
public class ModuloController {

    private static final Logger LOGGER = Logger.getLogger(ModuloController.class.getName());
    private final IModuloDAO moduloDAO;

    /**
     * Constructor del controlador.
     *
     * @param conexion conexión activa a la base de datos.
     */
    public ModuloController(Conexion conexion) {
        this.moduloDAO = new ModuloDAOImpl(conexion.getConexion());
    }

    /**
     * Obtiene la lista de módulos activos para un rol determinado.
     *
     * @param idRol ID del rol a consultar.
     * @return lista de módulos activos asociados al rol.
     */
    public List<Modulo> obtenerModulosPorRol(int idRol) {
        try {
            return moduloDAO.obtenerModulosPorRol(idRol);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener los módulos del rol con ID: " + idRol, ex);
            return List.of(); // Retorna lista vacía en caso de error
        }
    }
}
