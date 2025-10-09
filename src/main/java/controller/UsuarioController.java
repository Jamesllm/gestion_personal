package controller;

import dao.impl.Conexion;
import dao.impl.UsuarioDAOImpl;
import dao.interfaces.IUsuarioDAO;
import model.Usuario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador para la gestión de usuarios. Maneja las operaciones CRUD y de
 * autenticación.
 *
 * @author James
 */
public class UsuarioController {

    private static final Logger LOGGER = Logger.getLogger(UsuarioController.class.getName());
    private final IUsuarioDAO usuarioDAO;

    public UsuarioController(Conexion conexion) {
        this.usuarioDAO = new UsuarioDAOImpl(conexion.getConexion());
    }

    // === LOGIN ===
    public Usuario login(String username, String password) {
        try {
            return usuarioDAO.login(username, password);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al iniciar sesión", ex);
            return null;
        }
    }

    // === CREAR ===
    public boolean insertar(Usuario usuario) {
        try {
            return usuarioDAO.insertar(usuario);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al insertar usuario", ex);
            return false;
        }
    }

    // === ACTUALIZAR ===
    public boolean actualizar(Usuario usuario) {
        try {
            return usuarioDAO.actualizar(usuario);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al actualizar usuario", ex);
            return false;
        }
    }

    // === CAMBIAR PASSWORD ===
    public boolean cambiarPassword(int idUsuario, String nuevaContrasena) {
        try {
            return usuarioDAO.cambiarPassword(idUsuario, nuevaContrasena);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al cambiar contraseña", ex);
            return false;
        }
    }

    // === ELIMINAR ===
    public boolean eliminar(int idUsuario) {
        try {
            return usuarioDAO.eliminar(idUsuario);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al eliminar usuario", ex);
            return false;
        }
    }

    // === OBTENER POR ID ===
    public Usuario obtenerPorId(int idUsuario) {
        try {
            return usuarioDAO.obtenerPorId(idUsuario);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener usuario por ID", ex);
            return null;
        }
    }

    // === LISTAR TODOS ===
    public List<Usuario> listarTodos() {
        try {
            return usuarioDAO.listarTodos();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al listar usuarios", ex);
            return List.of();
        }
    }
}
