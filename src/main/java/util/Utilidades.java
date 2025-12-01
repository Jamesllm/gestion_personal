package util;

import java.awt.Color;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author Llapapasca Montes
 */
public class Utilidades {

    private static final Color COLOR_ACTIVO_FONDO = new Color(0, 121, 215); // Verde
    private static final Color COLOR_ACTIVO_TEXTO = Color.WHITE;

    private static final Color COLOR_INACTIVO_FONDO = new Color(236, 243, 248); // Gris oscuro
    private static final Color COLOR_INACTIVO_TEXTO = Color.BLACK;

    private static final String LOGO_PATH_BASE = "src/recursos/logos/";

    /**
     * Cambia el color de fondo y texto de un botón.
     *
     * @param boton El botón a modificar.
     * @param colorFondo Color de fondo.
     * @param colorTexto Color del texto.
     */
    public static void cambiarColorBoton(JButton boton, Color colorFondo, Color colorTexto) {
        boton.setOpaque(true); // Asegura que el fondo sea visible
        boton.setBorderPainted(false); // Opcional: elimina bordes si es necesario
        boton.setBackground(colorFondo);
        boton.setForeground(colorTexto);
    }

    /**
     * Actualiza los colores de un conjunto de botones, destacando uno como
     * activo.
     *
     * @param botonActivo El botón que se mostrará como activo.
     * @param botones Todos los botones del grupo.
     */
    public static void actualizarColoresBotones(JButton botonActivo, JButton... botones) {
        for (JButton boton : botones) {
            if (boton.equals(botonActivo)) {
                cambiarColorBoton(boton, COLOR_ACTIVO_FONDO, COLOR_ACTIVO_TEXTO);
            } else {
                cambiarColorBoton(boton, COLOR_INACTIVO_FONDO, COLOR_INACTIVO_TEXTO);
            }
        }
    }

    public static void setImageLabel(JLabel labelName, String root) {
        setImageLabel(labelName, root, labelName.getWidth(), labelName.getHeight());
    }

    public static void setImageLabel(JLabel labelName, String root, int width, int height) {
        try {
            java.net.URL imageURL = Utilidades.class.getResource(root);

            if (imageURL == null) {
                System.err.println("No se encontró la imagen: " + root);
                return;
            }

            ImageIcon image = new ImageIcon(imageURL);

            int finalWidth = width > 0 ? width : 210;
            int finalHeight = height > 0 ? height : 320;

            Icon icon = new ImageIcon(
                    image.getImage().getScaledInstance(finalWidth, finalHeight, Image.SCALE_SMOOTH)
            );
            labelName.setIcon(icon);
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
        }
    }

    public static void cargarLogo(JLabel label, String nombreLogo) {
        setImageLabel(label, LOGO_PATH_BASE + nombreLogo);
    }

}
