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

        String sql = "{ CALL sp_login_usuario(?, ?) }";

        try (CallableStatement cs = conexion.prepareCall(sql)) {
            cs.setString(1, username);
            cs.setString(2, password);
            try (ResultSet rs = cs.executeQuery()) {
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
        String sql = "{ ? = CALL sp_insertar_usuario(?, ?, ?, ?) }";
        try (CallableStatement cs = conexion.prepareCall(sql)) {
            cs.registerOutParameter(1, Types.BOOLEAN);
            cs.setString(2, usuario.getPassword());
            cs.setInt(3, usuario.getRol().getIdRol());
            cs.setInt(4, usuario.getIdEmpleado());
            cs.setBoolean(5, usuario.isCambiarPassword());
            cs.execute();
            return cs.getBoolean(1);
        }
    }

    // ======================
    // ACTUALIZAR USUARIO
    // ======================
    @Override
    public boolean actualizar(Usuario usuario) throws SQLException {
        String sql = "{ ? = CALL sp_actualizar_usuario(?, ?, ?, ?, ?) }";
        try (CallableStatement cs = conexion.prepareCall(sql)) {
            cs.registerOutParameter(1, Types.BOOLEAN);
            cs.setInt(2, usuario.getIdUsuario());
            cs.setString(3, usuario.getPassword());
            cs.setInt(4, usuario.getRol().getIdRol());
            cs.setInt(5, usuario.getIdEmpleado());
            cs.setBoolean(6, usuario.isCambiarPassword());
            cs.execute();
            return cs.getBoolean(1);
        }
    }

    @Override
    public boolean eliminar(int idUsuario) throws SQLException {
        String sql = "{ ? = CALL sp_eliminar_usuario(?) }";
        try (CallableStatement cs = conexion.prepareCall(sql)) {
            cs.registerOutParameter(1, Types.BOOLEAN);
            cs.setInt(2, idUsuario);
            cs.execute();
            return cs.getBoolean(1);
        }
    }

    // ======================
    // CAMBIAR CONTRASEÑA
    // ======================
    @Override
    public boolean cambiarPassword(int idUsuario, String nuevaContrasena) throws SQLException {
        String sql = "{ ? = CALL sp_cambiar_password(?, ?) }";
        try (CallableStatement cs = conexion.prepareCall(sql)) {
            cs.registerOutParameter(1, Types.BOOLEAN);
            cs.setInt(2, idUsuario);
            cs.setString(3, nuevaContrasena);
            cs.execute();
            return cs.getBoolean(1);
        }
    }

    // ======================
    // OBTENER POR ID
    // ======================
    @Override
    public Usuario obtenerPorId(int idUsuario) throws SQLException {
        Preconditions.checkArgument(idUsuario > 0, "El ID del usuario debe ser positivo");

        String sql = "{CALL sp_obtener_usuario_por_id(?)}";

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
        String sql = "{ CALL sp_listar_usuarios() }";

        try (CallableStatement cs = conexion.prepareCall(sql); ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }
        }
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
