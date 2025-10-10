import dao.impl.Conexion;
import dao.impl.UsuarioDAOImpl;
import model.Usuario;
import org.testng.annotations.*;
import static org.testng.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

public class UsuarioTestNG {

    private UsuarioDAOImpl dao;
    private Connection conexion;

    @BeforeClass
    public void setup() {
        conexion = Conexion.getInstance().getConexion();
        dao = new UsuarioDAOImpl(conexion);
    }

    @Test
    public void testObtenerPorId() {
        try {
            Usuario usuario = dao.obtenerPorId(1);
            assertNotNull(usuario, "El usuario no debe ser nulo.");
            System.out.println("✅ Usuario encontrado: " + usuario.getUsername());
        } catch (SQLException ex) {
            fail("Error al obtener el usuario: " + ex.getMessage());
        }
    }

    @AfterClass
    public void teardown() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
