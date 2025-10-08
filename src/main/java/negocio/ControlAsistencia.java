package negocio;

import com.formdev.flatlaf.FlatLightLaf;
import presentacion.Login;
import javax.swing.UIManager;

/**
 *
 * @author James
 */
public class ControlAsistencia {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());

            dao.impl.Conexion conexionDB = dao.impl.Conexion.getInstance();
            
            if (conexionDB.getConexion() != null) {
                System.out.println("Conexión exitosa a la base de datos PostgreSQL.");

                // Despues de obtener la conexion se habre la app
                Login login = new Login(conexionDB);
                login.setVisible(true);

            } else {
                System.out.println("No se pudo conectar a la base de datos.");
            }

        } catch (Exception ex) {
            System.err.println("Error al aplicar FlatLaf: " + ex.getMessage());
        }
    }
}
