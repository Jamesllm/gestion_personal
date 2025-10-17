package dao.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import dao.interfaces.IUsuarioDAO;
import model.Usuario;
import model.Rol;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de IUsuarioDAO usando Guava para validaciones seguras y listas
 * inmutables.
 */
public class UsuarioDAOImpl implements IUsuarioDAO {

    private final Connection conexion;

    public UsuarioDAOImpl(Connection conexion) {
        // Valida que la conexión no sea nula
        this.conexion = Preconditions.checkNotNull(conexion, "La conexión no puede ser nula");
    }

    // ======================
    // LOGIN
    // ======================
    @Override
    public Usuario login(String username, String password) throws SQLException {
        Preconditions.checkArgument(username != null && !username.isBlank(), "El nombre de usuario no puede estar vacío");
        Preconditions.checkArgument(password != null && !password.isBlank(), "La contraseña no puede estar vacía");

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
        Preconditions.checkNotNull(usuario, "El usuario no puede ser nulo");
        Preconditions.checkNotNull(usuario.getRol(), "El rol del usuario no puede ser nulo");

        String sql = """
            INSERT INTO usuario (contrasena, id_rol, id_empleado, cambiar_password)
            VALUES (?, ?, ?, ?)
        """;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, usuario.getPassword());
            ps.setInt(2, usuario.getRol().getIdRol());
            ps.setInt(3, usuario.getIdEmpleado());
            ps.setBoolean(4, usuario.isCambiarPassword());
            return ps.executeUpdate() > 0;
        }
    }

    // ======================
    // ACTUALIZAR USUARIO
    // ======================
    @Override
    public boolean actualizar(Usuario usuario) throws SQLException {
        Preconditions.checkNotNull(usuario, "El usuario no puede ser nulo");

        String sql = """
            UPDATE usuario 
            SET contrasena = ?, id_rol = ?, id_empleado = ?, cambiar_password = ?
            WHERE id_usuario = ?
        """;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, usuario.getPassword());
            ps.setInt(2, usuario.getRol().getIdRol());
            ps.setInt(3, usuario.getIdEmpleado());
            ps.setBoolean(4, usuario.isCambiarPassword());
            ps.setInt(5, usuario.getIdUsuario());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int idUsuario) throws SQLException {
        // Validar argumento antes de ejecutar la consulta
        Preconditions.checkArgument(idUsuario > 0, "El ID de usuario debe ser mayor que 0");

        String sql = "UPDATE usuario SET activo = false WHERE id_usuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            int filasAfectadas = ps.executeUpdate();

            // Validar que la operación realmente afectó un registro
            Preconditions.checkState(filasAfectadas > 0, "No se encontró el usuario con el ID especificado");

            return true;
        } catch (SQLException e) {
            throw new SQLException("Error al intentar eliminar el usuario: " + e.getMessage(), e);
        }
    }

    // ======================
    // CAMBIAR CONTRASEÑA
    // ======================
    @Override
    public boolean cambiarPassword(int idUsuario, String nuevaContrasena) throws SQLException {
        Preconditions.checkArgument(idUsuario > 0, "El ID del usuario debe ser positivo");
        Preconditions.checkArgument(nuevaContrasena != null && !nuevaContrasena.isBlank(), "La nueva contraseña no puede estar vacía");

        String sql = "UPDATE usuario SET contrasena = ?, cambiar_password = false WHERE id_usuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nuevaContrasena);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        }
    }

    // ======================
    // OBTENER POR ID
    // ======================
    @Override
    public Usuario obtenerPorId(int idUsuario) throws SQLException {
        Preconditions.checkArgument(idUsuario > 0, "El ID del usuario debe ser positivo");

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
                return rs.next() ? mapResultSet(rs) : null;
            }
        }
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
        // Retorna lista inmutable (no puede ser modificada accidentalmente)
        return ImmutableList.copyOf(lista);
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
