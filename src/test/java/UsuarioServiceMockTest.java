import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import dao.interfaces.IUsuarioDAO;
import model.Usuario;
import java.sql.SQLException;

public class UsuarioServiceMockTest {

    @Test
    void testLoginConMock() throws SQLException {
        // Creamos el mock de IUsuarioDAO
        IUsuarioDAO mockDao = mock(IUsuarioDAO.class);
        
        // Creamos un objeto Usuario simulado que devolverá el mock
        Usuario mockUser = new Usuario();
        mockUser.setUsername("ronal");
        mockUser.setPassword("1234"); 

        // Configuramos el mock para devolver el usuario cuando se llama a login con esos parámetros
        when(mockDao.login("12345678", "admin")).thenReturn(mockUser);
        Usuario result = mockDao.login("12345678", "admin");

        // Verificamos que el resultado sea el esperado
        assertNotNull(result, "El usuario autenticado no debe ser null.");
        assertEquals("ronal", result.getUsername(), "El nombre de usuario no coincide.");
        
        // Verificamos que el método login haya sido llamado con los parámetros correctos
        verify(mockDao).login("ronal", "1234");
    }
}
