package negocio;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ActualizadorFechaHora {

    private Timer timer;

    public ActualizadorFechaHora(JLabel lblFecha, JLabel lblHora) {
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern(
                "EEEE, d 'de' MMMM 'de' yyyy",
                Locale.of("es", "ES")
        );
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("hh:mm:ss a");

        timer = new Timer(1000, e -> {
            LocalDate hoy = LocalDate.now();
            LocalTime ahora = LocalTime.now();

            lblFecha.setText(hoy.format(formatoFecha));
            lblHora.setText(ahora.format(formatoHora));
        });
        timer.start();
    }

    public void detener() {
        if (timer != null) {
            timer.stop();
        }
    }
}
