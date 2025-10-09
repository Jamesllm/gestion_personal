
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import model.Usuario;

import dao.impl.Conexion;
import java.sql.Connection;
import dao.impl.LoginService;

public class UsuarioDAOTest {

    @Test
    void testLogin() {
        Connection conexion = Conexion.getInstance().getConexion();

        String username = "12345678";
        String password = "admin";

        Usuario u = null;
        try {
            u = LoginService.autenticarUsuario(conexion, username, password);
        } catch (Exception ex) {
            System.getLogger(UsuarioDAOTest.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        assertNotNull(u, "El usuario autenticado no debe ser null.");

        assertEquals("Ronal Llapapasca Montes", u.getUsername(), "El nombre de usuario no coincide.");
    }
}
