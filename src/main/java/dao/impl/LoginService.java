package dao.impl;

import java.sql.CallableStatement;
import model.Rol;
import model.Usuario;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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

        String sql = "{ CALL sp_autenticar_usuario(?) }";

        try (CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, dniUsuario);

            try (ResultSet rs = cs.executeQuery()) {
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

                        logger.info("✅ Autenticación exitosa para usuario con DNI: {}", dniUsuario);
                    } else {
                        logger.warn("❌ Contraseña incorrecta para usuario con DNI: {}", dniUsuario);
                    }
                } else {
                    logger.warn("⚠️ No se encontró usuario activo con DNI: {}", dniUsuario);
                }

                return usuario;
            }

        } catch (SQLException e) {
            logger.error("❌ Error SQL al autenticar usuario con DNI {}: {}", dniUsuario, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("⚠️ Error inesperado al autenticar usuario con DNI {}: {}", dniUsuario, e.getMessage());
            throw e;
        }
    }

    /**
     * Genera el hash de una contraseña.
     *
     * @param contrasenaPlana
     * @return
     */
    public static String hashearContrasena(String contrasenaPlana) {
        logger.debug("Hasheando contraseña para nuevo usuario...");
        return BCrypt.hashpw(contrasenaPlana, BCrypt.gensalt());
    }
}
