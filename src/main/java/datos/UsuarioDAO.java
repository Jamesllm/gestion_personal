package datos;

import clases.Usuario;
import clases.Rol;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private Connection conexion;

    public UsuarioDAO(Connection conexion) {
        this.conexion = conexion;
    }

    // ======================
    // LOGIN
    // ======================
    public Usuario login(String username, String password) {
        String sql = "SELECT u.id_usuario, u.username, u.password, u.cambiar_password, "
                + "r.id_rol, r.nombre_rol, "
                + "u.id_empleado "
                + "FROM usuario u "
                + "JOIN rol r ON u.id_rol = r.id_rol "
                + "WHERE u.username = ? AND u.password = ? AND u.activo = true";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setCambiarPassword(rs.getBoolean("cambiar_password"));
                u.setIdEmpleado(rs.getInt("id_empleado"));

                Rol rol = new Rol();
                rol.setIdRol(rs.getInt("id_rol"));
                rol.setNombreRol(rs.getString("nombre_rol"));
                u.setRol(rol);

                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ======================
    // CAMBIAR CONTRASEÑA
    // ======================
    public boolean cambiarPassword(int idUsuario, String nuevaContrasena) {
        String sql = "UPDATE usuario SET password = ?, cambiar_password = false WHERE id_usuario = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nuevaContrasena);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ======================
    // OBTENER POR ID
    // ======================
    public Usuario obtenerPorId(int idUsuario) {
        String sql = "SELECT u.id_usuario, u.username, u.password, u.cambiar_password, "
                + "r.id_rol, r.nombre_rol, "
                + "u.id_empleado "
                + "FROM usuario u "
                + "JOIN rol r ON u.id_rol = r.id_rol "
                + "WHERE u.id_usuario = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setCambiarPassword(rs.getBoolean("cambiar_password"));
                u.setIdEmpleado(rs.getInt("id_empleado"));

                Rol rol = new Rol();
                rol.setIdRol(rs.getInt("id_rol"));
                rol.setNombreRol(rs.getString("nombre_rol"));
                u.setRol(rol);

                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ======================
    // LISTAR TODOS
    // ======================
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = """
                     SELECT u.id_usuario, e.nombres as username, u.contrasena, u.cambiar_password,
                     r.id_rol, r.nombre_rol, 
                     u.id_empleado 
                     FROM usuario u
                     JOIN rol r ON u.id_rol = r.id_rol
                     JOIN empleado e ON u.id_empleado = e.id_empleado 
                     """;

        try (Statement st = conexion.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
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

                usuarios.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }
}
