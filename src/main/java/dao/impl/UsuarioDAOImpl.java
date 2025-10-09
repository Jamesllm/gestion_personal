package dao.impl;

import dao.interfaces.IUsuarioDAO;
import model.Usuario;
import model.Rol;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de IUsuarioDAO para manejar operaciones CRUD y de
 * autenticación.
 *
 * @author James
 */
public class UsuarioDAOImpl implements IUsuarioDAO {

    private final Connection conexion;

    public UsuarioDAOImpl(Connection conexion) {
        this.conexion = conexion;
    }

    // ======================
    // LOGIN
    // ======================
    @Override
    public Usuario login(String username, String password) throws SQLException {
        String sql = """
            SELECT u.id_usuario, e.nombres AS username, u.contrasena, u.cambiar_password, 
                   u.id_empleado, r.id_rol, r.nombre_rol
            FROM usuario u
            JOIN rol r ON u.id_rol = r.id_rol
            JOIN empleado e ON u.id_empleado = e.id_empleado
            WHERE e.nombres = ? AND u.contrasena = ? AND u.activo = TRUE
        """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        }
        return null;
    }

    // ======================
    // INSERTAR NUEVO USUARIO
    // ======================
    @Override
    public boolean insertar(Usuario usuario) throws SQLException {
        String sql = """
            INSERT INTO usuario (contrasena, id_rol, id_empleado, cambiar_password)
            VALUES (?, ?, ?, ?)
        """;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, usuario.getPassword());
            ps.setInt(2, usuario.getRol().getIdRol());
            ps.setInt(3, usuario.getIdEmpleado());
            ps.setBoolean(5, usuario.isCambiarPassword());
            return ps.executeUpdate() > 0;
        }
    }

    // ======================
    // ACTUALIZAR USUARIO
    // ======================
    @Override
    public boolean actualizar(Usuario usuario) throws SQLException {
        String sql = """
            UPDATE usuario 
            SET contrasena = ?, id_rol = ?, id_empleado = ?, cambiar_password = ?
            WHERE id_usuario = ?
        """;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, usuario.getPassword());
            ps.setInt(2, usuario.getRol().getIdRol());
            ps.setInt(3, usuario.getIdEmpleado());
            ps.setBoolean(5, usuario.isCambiarPassword());
            ps.setInt(6, usuario.getIdUsuario());
            return ps.executeUpdate() > 0;
        }
    }

    // ======================
    // CAMBIAR CONTRASEÑA
    // ======================
    @Override
    public boolean cambiarPassword(int idUsuario, String nuevaContrasena) throws SQLException {
        String sql = "UPDATE usuario SET contrasena = ?, cambiar_password = false WHERE id_usuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nuevaContrasena);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        }
    }

    // ======================
    // ELIMINAR (DESACTIVAR)
    // ======================
    @Override
    public boolean eliminar(int idUsuario) throws SQLException {
        String sql = "UPDATE usuario SET activo = false WHERE id_usuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        }
    }

    // ======================
    // OBTENER POR ID
    // ======================
    @Override
    public Usuario obtenerPorId(int idUsuario) throws SQLException {
        String sql = """
            SELECT u.id_usuario, e.nombres AS username, u.contrasena, u.cambiar_password,
                   r.id_rol, r.nombre_rol, u.id_empleado, u.activo
            FROM usuario u
            JOIN rol r ON u.id_rol = r.id_rol
            JOIN empleado e ON u.id_empleado = e.id_empleado
            WHERE u.id_usuario = ?
        """;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        }
        return null;
    }

    // ======================
    // LISTAR TODOS
    // ======================
    @Override
    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = """
            SELECT u.id_usuario, e.nombres AS username, u.contrasena, u.cambiar_password,
                   r.id_rol, r.nombre_rol, u.id_empleado, u.activo
            FROM usuario u
            JOIN rol r ON u.id_rol = r.id_rol
            JOIN empleado e ON u.id_empleado = e.id_empleado
            ORDER BY u.id_usuario DESC
        """;

        try (Statement st = conexion.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }
        }
        return lista;
    }

    // ======================
    // MAPEO RESULTSET → OBJETO
    // ======================
    private Usuario mapResultSet(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("contrasena"));
        u.setCambiarPassword(rs.getBoolean("cambiar_password"));
        u.setIdEmpleado(rs.getInt("id_empleado"));

        Rol rol = new Rol();
        rol.setIdRol(rs.getInt("id_rol"));
        rol.setNombreRol(rs.getString("nombre_rol"));
        u.setRol(rol);

        return u;
    }
}
