import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import model.Usuario;
import dao.impl.Conexion;
import dao.impl.LoginService;
import java.sql.Connection;

/**
 * Unit Test & Param Test para la autenticación de usuarios.
 */
public class UsuarioDAOTest {

    /**
     * Unit Test: prueba un único caso de login correcto.
     */
    @Test
    void testLogin() {
        Connection conexion = Conexion.getInstance().getConexion();

        String username = "12345678";
        String password = "admin";

        Usuario u = null;
        try {
            u = LoginService.autenticarUsuario(conexion, username, password);
        } catch (Exception ex) {
            System.getLogger(UsuarioDAOTest.class.getName())
                    .log(System.Logger.Level.ERROR, (String) null, ex);
        }

        assertNotNull(u, "El usuario autenticado no debe ser null.");
        assertEquals("Ronal Llapapasca Montes", u.getUsername(), "El nombre de usuario no coincide.");
    }

    /**
     * Param Test: prueba varios casos de login (válidos e inválidos).
     */
    @ParameterizedTest
    @CsvSource({
        "12345678, admin, true",
        "87654321, wrongpass, false",
        "00000000, admin, false",
        "12345678, , false"
    })
    void testLoginParametrizado(String username, String password, boolean esperadoExito) {
        Connection conexion = Conexion.getInstance().getConexion();

        Usuario u = null;
        try {
            u = LoginService.autenticarUsuario(conexion, username, password);
        } catch (Exception ex) {
            System.getLogger(UsuarioDAOTest.class.getName())
                    .log(System.Logger.Level.ERROR, (String) null, ex);
        }

        if (esperadoExito) {
            assertNotNull(u, "El usuario debería autenticarse correctamente.");
        } else {
            assertNull(u, "El usuario no debería autenticarse con credenciales incorrectas.");
        }
    }
}
