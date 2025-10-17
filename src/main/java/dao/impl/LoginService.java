package dao.impl;

import model.Rol;
import model.Usuario;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    /**
     * Autentica a un usuario usando su nombre de usuario y contraseña.
     *
     * @param conn Conexión a la base de datos.
     * @param dniUsuario DNI del usuario ingresado.
     * @param contrasenaIngresada Contraseña ingresada.
     * @return Usuario autenticado o null si falla.
     * @throws Exception si ocurre un error en la base de datos.
     */
    public static Usuario autenticarUsuario(Connection conn, String dniUsuario, String contrasenaIngresada) throws Exception {
        logger.info("Intentando autenticar usuario con DNI: {}", dniUsuario);

        String sql = """
            SELECT
                u.id_usuario,
                e.nombres || ' ' || e.apellidos AS nombre_completo,
                u.contrasena,
                u.id_rol,
                r.nombre_rol,
                u.id_empleado
            FROM Usuario u
            JOIN Empleado e ON u.id_empleado = e.id_empleado
            JOIN Rol r ON u.id_rol = r.id_rol
            WHERE e.dni = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dniUsuario);
            ResultSet rs = ps.executeQuery();

            Usuario usuario = null;
            if (rs.next()) {
                String contrasenaHashBd = rs.getString("contrasena");
                if (BCrypt.checkpw(contrasenaIngresada, contrasenaHashBd)) {
                    Rol rol = new Rol(
                            rs.getInt("id_rol"),
                            rs.getString("nombre_rol")
                    );

                    usuario = new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("nombre_completo"),
                            rs.getString("contrasena"),
                            rol,
                            rs.getInt("id_empleado")
                    );

                    logger.info("✅ Autenticación exitosa para usuario: {}", usuario.getUsername());
                } else {
                    logger.warn("❌ Contraseña incorrecta para usuario con DNI: {}", dniUsuario);
                }
            } else {
                logger.warn("❌ No se encontró usuario con DNI: {}", dniUsuario);
            }

            return usuario;

        } catch (Exception e) {
            logger.error("❌ Error al autenticar usuario con DNI: {}", dniUsuario, e);
            throw e;
        }
    }

    /**
     * Genera el hash de una contraseña.
     * @param contrasenaPlana
     * @return 
     */
    public static String hashearContrasena(String contrasenaPlana) {
        logger.debug("Hasheando contraseña para nuevo usuario...");
        return BCrypt.hashpw(contrasenaPlana, BCrypt.gensalt());
    }
}
