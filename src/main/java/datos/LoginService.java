package datos;

import clases.Rol;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import clases.Usuario; // Asegúrate de que esta clase esté bien definida
import org.mindrot.jbcrypt.BCrypt; // Importar la clase BCrypt

public class LoginService {

    /**
     * Autentica a un usuario usando su nombre de usuario y contraseña. La
     * contraseña ingresada es comparada con el hash almacenado en la base de
     * datos usando BCrypt.
     *
     * @param conn La conexión a la base de datos.
     * @param nombreUsuario El nombre de usuario ingresado por el usuario.
     * @param contrasenaIngresada La contraseña en texto plano ingresada por el
     * usuario.
     * @return Un objeto Usuario si la autenticación es exitosa, de lo
     * contrario, null.
     * @throws Exception si ocurre un error en la base de datos.
     */
    // Este es tu método actual, que ya está bien
    public static Usuario autenticarUsuario(Connection conn, String dniUsuario, String contrasenaIngresada) throws Exception {

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

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, dniUsuario);

        ResultSet rs = ps.executeQuery();
        Usuario usuario = null;

        if (rs.next()) {
            String contrasenaHashBd = rs.getString("contrasena");
            // Validación de la contraseña con BCrypt
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
            }
        }

        rs.close();
        ps.close();

        // Retorna el objeto usuario (si la autenticación fue exitosa) o null (si falló)
        return usuario;
    }

    /**
     * Este método es para hashear una contraseña antes de guardarla en la base
     * de datos. Deberías usarlo cuando registres nuevos usuarios o actualices
     * contraseñas.
     *
     * @param contrasenaPlana La contraseña en texto plano.
     * @return El hash de la contraseña.
     */
    public static String hashearContrasena(String contrasenaPlana) {
        return BCrypt.hashpw(contrasenaPlana, BCrypt.gensalt());
    }
}
