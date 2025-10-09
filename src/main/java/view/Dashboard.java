package view;

// === CONTROLLER
import controller.AsistenciaController;
import controller.EmpleadoController;
import controller.ModuloController;

// === MODEL
import model.Asistencia;
import model.Empleado;
import model.Inventario;
import model.Modulo;
import model.Usuario;

// === DAO
import dao.impl.InventarioDAOImpl;
import dao.impl.UsuarioDAOImpl;
import dao.impl.Conexion;

// === SERVICE
import service.ActualizadorFechaHora;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import java.text.NumberFormat;
import java.text.ParseException;

import java.sql.SQLException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.Locale;
import java.util.Map;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author James
 */
public class Dashboard extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Dashboard.class.getName());
    // Panel que contendrá las cards dentro del scroll
    private JPanel contenedorActividad;

    /**
     * Creates new form Dashboard
     */
    private Conexion conexionDB;
    private Usuario usuarioAutenticado;

    public Dashboard(Usuario usuarioAutenticado, Conexion conexionDB) throws SQLException {
        initComponents();
        this.setLocationRelativeTo(this);
        this.conexionDB = conexionDB;
        this.usuarioAutenticado = usuarioAutenticado;

        // 
        contenedorActividad = new JPanel();
        contenedorActividad.setLayout(new BoxLayout(contenedorActividad, BoxLayout.Y_AXIS));
        contenedorActividad.setBackground(new Color(236, 243, 248));
        contenedorActividad.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Vincular al JScrollPane
        panel_actividad_reciente.setViewportView(contenedorActividad);
        // Ajustar velocidad del scroll vertical
        panel_actividad_reciente.getVerticalScrollBar().setUnitIncrement(30);

        // == VERIFICAR ROLES ==
        ModuloController moduloDAO = new ModuloController(conexionDB);
        List<Modulo> modulosActivos = moduloDAO.obtenerModulosPorRol(usuarioAutenticado.getRol().getIdRol());

        // Ocultar todo por defecto
        btnInicio.setVisible(false);
        btnEmpleados.setVisible(false);
        btnAsistencia.setVisible(false);
        btnReportes.setVisible(false);
        btnInventario.setVisible(false);

        // Mostrar solo lo que tenga activo en BD
        for (Modulo m : modulosActivos) {
            switch (m.getNombreModulo()) {
                case "Inicio" ->
                    btnInicio.setVisible(true);
                case "Empleados" ->
                    btnEmpleados.setVisible(true);
                case "Asistencia" ->
                    btnAsistencia.setVisible(true);
                case "Reportes" ->
                    btnReportes.setVisible(true);
                case "Inventario" ->
                    btnInventario.setVisible(true);
            }
        }

        util.Utilidades.actualizarColoresBotones(btnInicio, btnInicio, btnEmpleados, btnAsistencia, btnReportes, btnInventario, btnUsuario);

        // Cargar el nombre
        txt_bienvenida.setText("Bienvenido " + usuarioAutenticado.getUsername());
        txt_bienvenida1.setText("Bienvenido " + usuarioAutenticado.getUsername());
        nombre_usuario.setText(usuarioAutenticado.getUsername());
        rol_usuario.setText(usuarioAutenticado.getRol().getNombreRol());

        // === SECCION FECHA ===
        // Cargar la fecha actual en formato: "jueves, 25 de septiembre de 2025"
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern(
                "EEEE, d 'de' MMMM 'de' yyyy",
                Locale.of("es", "ES")
        );

        String fechaFormateada = hoy.format(formatoFecha);

        // Asignar al componente de interfaz
        fecha_actual.setText(fechaFormateada);
        fecha_actual1.setText(fechaFormateada);

        // === SECCION HORA SISTEMA ===
        // Obtener la hora actual
        LocalTime ahora = LocalTime.now();

        // Definir formato con horas 12h + minutos + segundos + AM/PM
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("hh:mm:ss a");

        // Formatear la hora actual
        String horaFormateada = ahora.format(formatoHora);

        // Asignar al componente de interfaz
        hora_actual.setText(horaFormateada);
        hora_actual1.setText(horaFormateada);

        new ActualizadorFechaHora(fecha_actual, hora_actual);
        new ActualizadorFechaHora(fecha_actual1, hora_actual1);

        // === SECCION EMPLEADOS ===
        // Cargar el total de empleados
        EmpleadoController empleadoController = new EmpleadoController(conexionDB);
        lbl_total_empleados.setText(String.valueOf(empleadoController.contarTotalEmpleados()));
        lbl_total_empleados2.setText(String.valueOf(empleadoController.contarTotalEmpleados()));

        double horas = empleadoController.obtenerHorasTotalesHoy();
        lbl_horas_trabajadas.setText(String.format("%.2f", horas));

        lbl_presentes_hoy.setText(String.valueOf(empleadoController.contarPresentesHoy()));
        lbl_presentes_hoy1.setText(String.valueOf(empleadoController.contarPresentesHoy()));
        lbl_presentes_hoy2.setText(String.valueOf(empleadoController.contarPresentesHoy()));

        lbl_horas_trabajadas1.setText(String.format("%.2f", horas));

        lbl_total_ausentes.setText(String.valueOf(empleadoController.contarAusentesHoy()));

        double horas_promedio = empleadoController.obtenerPromedioHorasHoy();
        lbl_horas_promedio.setText(String.format("%.2f", horas_promedio));

        // Cargar empleados en tabla
        cargarEmpleadosEnTabla(empleadoController);

        UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl(conexionDB.getConexion());
        cargarUsuariosEnTabla(usuarioDAO);

        // === MOSTRAR ACTIVIDAD RECIENTE == 
        // mostrar en este panel: panel_actividad_reciente
        List<String> actividadReciente = empleadoController.obtenerActividadReciente(10);

        // Renderizar en el panel
        renderizarActividadReciente(actividadReciente);

        // === SECCION ASISTENCIA ===
        cargarTablaAsistencia();

        // === SECCION REPORTES ===
        // Crear contenedor para los cards
        JPanel contenedorDepartamentos = new JPanel();
        contenedorDepartamentos.setLayout(new BoxLayout(contenedorDepartamentos, BoxLayout.Y_AXIS));
        contenedorDepartamentos.setBackground(new Color(236, 243, 248));
        contenedorDepartamentos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Vincular al JScrollPane
        panel_dis_departamento.setViewportView(contenedorDepartamentos);
        // Ajustar velocidad del scroll vertical
        panel_dis_departamento.getVerticalScrollBar().setUnitIncrement(30);

        // Renderizar distribución
        Map<String, Integer> distribucion = empleadoController.obtenerDistribucionPorDepartamento();
        renderizarDistribucionDepartamentos(distribucion, contenedorDepartamentos);

        // === SECCION REPORTES ===
        // panel_dis_mensual
        // Crear contenedor para los cards
        JPanel contenedorAsistenciaMensual = new JPanel();
        contenedorAsistenciaMensual.setLayout(new BoxLayout(contenedorAsistenciaMensual, BoxLayout.Y_AXIS));
        contenedorAsistenciaMensual.setBackground(new Color(236, 243, 248));
        contenedorAsistenciaMensual.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Vincular al JScrollPane
        panel_dis_mensual.setViewportView(contenedorAsistenciaMensual);
        // Ajustar velocidad del scroll vertical
        panel_dis_mensual.getVerticalScrollBar().setUnitIncrement(30);

        // Renderizar distribución
        Map<String, Integer> asistencia = empleadoController.obtenerAsistenciaMensual();
        renderizarAsistenciaMensual(asistencia, contenedorAsistenciaMensual);

        // === SECCION INVENTARIO ===
        // RENDERIZAR EN TABLA_: tablaInventario
        cargarInventarioEnTabla();
    }

    public void cargarInventarioEnTabla() {
        try {
            InventarioDAOImpl dao = new InventarioDAOImpl(conexionDB.getConexion());
            List<Inventario> lista = dao.listar();

            // Definir modelo de la tabla
            String[] columnas = {"ID", "Nombre", "Stock Actual", "Unidad", "Ubicación", "Stock Mínimo", "Precio Unitario", "Estado"};
            DefaultTableModel modelo = new DefaultTableModel(null, columnas);

            // Rellenar con datos
            NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "PE"));

            for (Inventario inv : lista) {
                Object[] fila = new Object[8];
                fila[0] = inv.getIdItem();
                fila[1] = inv.getNombreItem();
                fila[2] = inv.getStockActual();
                fila[3] = inv.getUnidad();
                fila[4] = inv.getUbicacion();
                fila[5] = inv.getStockMinimo();
                fila[6] = formatoMoneda.format(inv.getPrecioUnitario());
                fila[7] = inv.isEstado() ? "Activo" : "Inactivo";
                modelo.addRow(fila);
            }

            // Asignar modelo a la tabla
            tablaInventario.setModel(modelo);

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Error al cargar inventario: " + e.getMessage());
        }
    }

    private void renderizarAsistenciaMensual(Map<String, Integer> asistencia, JPanel contenedorMensual) {
        contenedorMensual.removeAll();

        for (Map.Entry<String, Integer> entry : asistencia.entrySet()) {
            String mes = entry.getKey();
            int cantidad = entry.getValue();

            // === CREAR CARD ===
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                    BorderFactory.createEmptyBorder(15, 20, 15, 20)
            ));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel lblMes = new JLabel("Mes: " + mes);
            lblMes.setFont(new Font("Segoe UI", Font.BOLD, 16));

            JLabel lblCantidad = new JLabel("Asistencias: " + cantidad);
            lblCantidad.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblCantidad.setForeground(new Color(70, 70, 70));

            card.add(lblMes);
            card.add(lblCantidad);

            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));

            contenedorMensual.add(card);
            contenedorMensual.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        contenedorMensual.revalidate();
        contenedorMensual.repaint();
    }

    private void renderizarDistribucionDepartamentos(Map<String, Integer> distribucion, JPanel contenedorDepartamentos) {
        contenedorDepartamentos.removeAll();

        for (Map.Entry<String, Integer> entry : distribucion.entrySet()) {
            String departamento = entry.getKey();
            int cantidad = entry.getValue();

            // === CREAR CARD ===
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                    BorderFactory.createEmptyBorder(15, 20, 15, 20) // padding interno
            ));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel lblDepartamento = new JLabel(departamento);
            lblDepartamento.setFont(new Font("Segoe UI", Font.BOLD, 16));

            JLabel lblCantidad = new JLabel("Empleados: " + cantidad);
            lblCantidad.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblCantidad.setForeground(new Color(70, 70, 70));

            card.add(lblDepartamento);
            card.add(lblCantidad);

            // Expandir ancho
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));

            contenedorDepartamentos.add(card);
            contenedorDepartamentos.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        contenedorDepartamentos.revalidate();
        contenedorDepartamentos.repaint();
    }

    private void renderizarActividadReciente(List<String> actividadReciente) {
        contenedorActividad.removeAll();

        for (String actividad : actividadReciente) {
            // Separar en partes
            String[] partes = actividad.split("\\|"); // divide por " | "

            // Primer bloque: "[2025-09-27] Juan Pérez - Entrada: 10:10:00"
            String bloque1 = partes[0].trim();
            String salida = partes.length > 1 ? partes[1].trim() : "";
            String estado = partes.length > 2 ? partes[2].trim().replace("Estado: ", "") : "";

            // Ahora parseamos mejor el bloque1
            String[] subPartes = bloque1.split("- Entrada:");
            String empleadoConFecha = subPartes[0].trim();  // "[2025-09-27] Juan Pérez"
            String entrada = subPartes.length > 1 ? "Entrada:" + subPartes[1].trim() : "";

            // === CREAR CARD ===
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15) // padding
            ));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel lblEmpleado = new JLabel(empleadoConFecha);
            lblEmpleado.setFont(new Font("Segoe UI", Font.BOLD, 16));

            JLabel lblEntrada = new JLabel(entrada);
            JLabel lblSalida = new JLabel(salida);
            JLabel lblEstado = new JLabel(estado);

            lblEntrada.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblSalida.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 14));

            // Colores para el estado
            if (estado.equalsIgnoreCase("Finalizado")) {
                lblEstado.setForeground(new Color(0, 128, 0)); // verde
            } else {
                lblEstado.setForeground(Color.RED); // rojo
            }

            // Agregar al card
            card.add(lblEmpleado);
            if (!entrada.isEmpty()) {
                card.add(lblEntrada);
            }
            if (!salida.isEmpty()) {
                card.add(lblSalida);
            }
            if (!estado.isEmpty()) {
                card.add(lblEstado);
            }

            // Expandir ancho para que ocupe todo
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));

            contenedorActividad.add(card);
            contenedorActividad.add(Box.createRigidArea(new Dimension(0, 10))); // espacio entre cards
        }

        contenedorActividad.revalidate();
        contenedorActividad.repaint();
    }

    public void cargarTablaAsistencia() {
        try {
            AsistenciaController dao = new AsistenciaController(conexionDB);
            List<Asistencia> asistencias;

            if (usuarioAutenticado.getRol().getNombreRol().equalsIgnoreCase("Administrador")) {
                asistencias = dao.obtenerTodas();
            } else {
                asistencias = dao.obtenerPorEmpleado(usuarioAutenticado.getIdEmpleado());
            }

            // Definir columnas de la tabla
            String[] columnas = {"ID", "Empleado", "Fecha", "Hora Entrada", "Hora Salida", "Estado"};
            DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

            for (Asistencia a : asistencias) {
                // ⚠️ aquí necesitas el nombre del empleado
                // Si tu tabla `asistencia` solo guarda id_empleado, deberías hacer un JOIN en el DAO
                // Por ahora coloco el id, pero puedes adaptar para traer el nombre desde EmpleadoDAO
                Object[] fila = {
                    a.getIdAsistencia(),
                    a.getIdEmpleado(), // cambia por nombre completo si haces el JOIN
                    a.getFecha(),
                    a.getHoraEntrada(),
                    a.getHoraSalida() != null ? a.getHoraSalida() : "N/A",
                    a.getEstado()
                };
                modelo.addRow(fila);
            }

            tablaAsistencia.setModel(modelo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarEmpleadosEnTabla(EmpleadoController empleadoDAO) {

        // Obtener la lista de todos los empleados
        List<Empleado> listaEmpleados = empleadoDAO.obtenerEmpleados();

        // Definir las columnas del JTable
        String[] columnNames = {"ID", "Nombres", "Apellidos", "DNI", "Correo", "Teléfono", "Depto ID", "Estado"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Iterar sobre la lista de empleados y agregar cada uno como una fila
        for (Empleado empleado : listaEmpleados) {
            Object[] rowData = {
                empleado.getIdEmpleado(),
                empleado.getNombres(),
                empleado.getApellidos(),
                empleado.getDni(),
                empleado.getCorreoElectronico(),
                empleado.getTelefono(),
                empleado.getIdDepartamento(),
                empleado.isEstado() ? "Activo" : "Inactivo" // Muestra 'Activo' o 'Inactivo' en lugar de true/false
            };
            model.addRow(rowData);
        }

        // Asignar el modelo a tu JTable
        tablaEmpleados.setModel(model);

    }

    public void cargarUsuariosEnTabla(UsuarioDAOImpl usuarioDAO) {
        try {
            // Obtener la lista de todos los usuarios
            List<Usuario> listaUsuarios = usuarioDAO.listarTodos();

            // Definir las columnas del JTable
            String[] columnNames = {"ID Usuario", "Username", "Rol", "ID Empleado", "¿Cambiar Password?"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            // Iterar sobre la lista de usuarios y agregar cada uno como una fila
            for (Usuario usuario : listaUsuarios) {
                Object[] rowData = {
                    usuario.getIdUsuario(),
                    usuario.getUsername(),
                    usuario.getRol() != null ? usuario.getRol().getNombreRol() : "N/A",
                    usuario.getIdEmpleado(),
                    usuario.isCambiarPassword() ? "Sí" : "No"
                };
                model.addRow(rowData);
            }

            // Asignar el modelo a tu JTable
            tablaUsuarios.setModel(model);

        } catch (Exception e) {
            System.err.println("Error al cargar los usuarios en la tabla: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error al cargar los datos de usuarios.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelTab = new javax.swing.JTabbedPane();
        jPanel_Inicio = new javax.swing.JPanel();
        txt_bienvenida = new javax.swing.JLabel();
        fecha_actual = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        hora_actual = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        lbl_total_empleados = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        lbl_presentes_hoy = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        lbl_horas_trabajadas = new javax.swing.JLabel();
        panel_actividad_reciente = new javax.swing.JScrollPane();
        jPanel_Empleados = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tablaEmpleados = new javax.swing.JTable();
        lbl_atender_ahora3 = new javax.swing.JLabel();
        lbl_atender_ahora6 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnEliminarEmpleado = new javax.swing.JButton();
        btnEditarEmpleado = new javax.swing.JButton();
        btnAgregarEmpleado = new javax.swing.JButton();
        jPanel_Asistencia = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tablaAsistencia = new javax.swing.JTable();
        hora_actual1 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lbl_presentes_hoy2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lbl_total_ausentes = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        lbl_horas_promedio = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txt_bienvenida1 = new javax.swing.JLabel();
        fecha_actual1 = new javax.swing.JLabel();
        jPanel_Reportes = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        lbl_total_empleados2 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        lbl_presentes_hoy1 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        lbl_horas_trabajadas1 = new javax.swing.JLabel();
        lbl_atender_ahora4 = new javax.swing.JLabel();
        lbl_atender_ahora7 = new javax.swing.JLabel();
        btnGenerarReporte = new javax.swing.JButton();
        panel_dis_mensual = new javax.swing.JScrollPane();
        panel_dis_departamento = new javax.swing.JScrollPane();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel_Inventario = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tablaInventario = new javax.swing.JTable();
        lbl_atender_ahora5 = new javax.swing.JLabel();
        lbl_atender_ahora8 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        btnEliminarProducto = new javax.swing.JButton();
        btnEditarProducto = new javax.swing.JButton();
        btnAgregarEmpleado1 = new javax.swing.JButton();
        jPanel_Usuarios = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tablaUsuarios = new javax.swing.JTable();
        lbl_atender_ahora9 = new javax.swing.JLabel();
        lbl_atender_ahora10 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        btnEliminarUsuario = new javax.swing.JButton();
        btnEditarUsuario = new javax.swing.JButton();
        btnAgregarUsuario = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btnInicio = new javax.swing.JButton();
        btnEmpleados = new javax.swing.JButton();
        btnAsistencia = new javax.swing.JButton();
        btnReportes = new javax.swing.JButton();
        btnInventario = new javax.swing.JButton();
        btnUsuario = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        nombre_usuario = new javax.swing.JLabel();
        rol_usuario = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        btnCerrarSesion = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Dashboard - MTD");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel_Inicio.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_Inicio.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_bienvenida.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        txt_bienvenida.setText("Inicio");
        jPanel_Inicio.add(txt_bienvenida, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 19, -1, -1));

        fecha_actual.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        fecha_actual.setText("fecha actual");
        jPanel_Inicio.add(fecha_actual, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 467, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("Actividad reciente");
        jPanel_Inicio.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 538, -1));

        hora_actual.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        hora_actual.setText("hora_actual");
        jPanel_Inicio.add(hora_actual, new org.netbeans.lib.awtextra.AbsoluteConstraints(726, 40, 188, -1));

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setLayout(new java.awt.GridLayout(1, 0, 10, 0));

        jPanel14.setBackground(new java.awt.Color(236, 243, 248));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Total de empleados");

        lbl_total_empleados.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lbl_total_empleados.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_total_empleados.setText("12");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                    .addComponent(lbl_total_empleados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lbl_total_empleados)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel13.add(jPanel14);

        jPanel15.setBackground(new java.awt.Color(236, 243, 248));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Presentes hoy");

        lbl_presentes_hoy.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lbl_presentes_hoy.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_presentes_hoy.setText("12");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
                    .addComponent(lbl_presentes_hoy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lbl_presentes_hoy)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel13.add(jPanel15);

        jPanel16.setBackground(new java.awt.Color(236, 243, 248));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Horas trabajadas");

        lbl_horas_trabajadas.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lbl_horas_trabajadas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_horas_trabajadas.setText("12");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_horas_trabajadas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lbl_horas_trabajadas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel13.add(jPanel16);

        jPanel_Inicio.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 900, -1));

        panel_actividad_reciente.setBackground(new java.awt.Color(236, 243, 248));
        jPanel_Inicio.add(panel_actividad_reciente, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 900, 430));

        PanelTab.addTab("Inicio", jPanel_Inicio);

        jPanel_Empleados.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_Empleados.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaEmpleados.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tablaEmpleados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane6.setViewportView(tablaEmpleados);

        jPanel_Empleados.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 116, 910, 590));

        lbl_atender_ahora3.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lbl_atender_ahora3.setText("Gestion de empleados");
        jPanel_Empleados.add(lbl_atender_ahora3, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 19, -1, -1));

        lbl_atender_ahora6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbl_atender_ahora6.setText("Administra la información de todos los empleados");
        jPanel_Empleados.add(lbl_atender_ahora6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        jPanel1.setLayout(new java.awt.GridLayout(1, 0, 5, 0));

        btnEliminarEmpleado.setBackground(new java.awt.Color(255, 51, 51));
        btnEliminarEmpleado.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnEliminarEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarEmpleado.setText("Eliminar");
        btnEliminarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarEmpleadoActionPerformed(evt);
            }
        });
        jPanel1.add(btnEliminarEmpleado);

        btnEditarEmpleado.setBackground(new java.awt.Color(255, 153, 102));
        btnEditarEmpleado.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnEditarEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        btnEditarEmpleado.setText("Editar");
        btnEditarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarEmpleadoActionPerformed(evt);
            }
        });
        jPanel1.add(btnEditarEmpleado);

        btnAgregarEmpleado.setBackground(new java.awt.Color(0, 121, 216));
        btnAgregarEmpleado.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnAgregarEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarEmpleado.setText("Nuevo empleado");
        btnAgregarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarEmpleadoActionPerformed(evt);
            }
        });
        jPanel1.add(btnAgregarEmpleado);

        jPanel_Empleados.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 40, 420, 50));

        PanelTab.addTab("Empleados", jPanel_Empleados);

        jPanel_Asistencia.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_Asistencia.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaAsistencia.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tablaAsistencia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane7.setViewportView(tablaAsistencia);

        jPanel_Asistencia.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 292, 899, 420));

        hora_actual1.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        hora_actual1.setText("hora_actual");
        jPanel_Asistencia.add(hora_actual1, new org.netbeans.lib.awtextra.AbsoluteConstraints(726, 40, 188, -1));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setLayout(new java.awt.GridLayout(1, 0, 10, 0));

        jPanel4.setBackground(new java.awt.Color(236, 243, 248));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Presentes hoy");

        lbl_presentes_hoy2.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lbl_presentes_hoy2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_presentes_hoy2.setText("12");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                    .addComponent(lbl_presentes_hoy2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lbl_presentes_hoy2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel4);

        jPanel6.setBackground(new java.awt.Color(236, 243, 248));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Ausentes");

        lbl_total_ausentes.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lbl_total_ausentes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_total_ausentes.setText("12");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
                    .addComponent(lbl_total_ausentes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lbl_total_ausentes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel6);

        jPanel7.setBackground(new java.awt.Color(236, 243, 248));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Horas promedio");

        lbl_horas_promedio.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lbl_horas_promedio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_horas_promedio.setText("12");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_horas_promedio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lbl_horas_promedio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel7);

        jPanel_Asistencia.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 900, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setText("Registro de asisencia -  viernes, 25 de septiembre de 2025");
        jPanel_Asistencia.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, -1, -1));

        txt_bienvenida1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        txt_bienvenida1.setText("Inicio");
        jPanel_Asistencia.add(txt_bienvenida1, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 19, -1, -1));

        fecha_actual1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        fecha_actual1.setText("fecha actual");
        jPanel_Asistencia.add(fecha_actual1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 467, -1));

        PanelTab.addTab("Asistencia", jPanel_Asistencia);

        jPanel_Reportes.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_Reportes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setLayout(new java.awt.GridLayout(1, 0, 10, 0));

        jPanel10.setBackground(new java.awt.Color(236, 243, 248));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Total de empleados");

        lbl_total_empleados2.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lbl_total_empleados2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_total_empleados2.setText("12");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                    .addComponent(lbl_total_empleados2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lbl_total_empleados2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel10);

        jPanel11.setBackground(new java.awt.Color(236, 243, 248));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Presentes hoy");

        lbl_presentes_hoy1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lbl_presentes_hoy1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_presentes_hoy1.setText("12");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
                    .addComponent(lbl_presentes_hoy1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lbl_presentes_hoy1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel11);

        jPanel12.setBackground(new java.awt.Color(236, 243, 248));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Horas trabajadas");

        lbl_horas_trabajadas1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lbl_horas_trabajadas1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_horas_trabajadas1.setText("12");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_horas_trabajadas1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lbl_horas_trabajadas1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel12);

        jPanel_Reportes.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 900, -1));

        lbl_atender_ahora4.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lbl_atender_ahora4.setText("Reportes");
        jPanel_Reportes.add(lbl_atender_ahora4, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 19, -1, -1));

        lbl_atender_ahora7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbl_atender_ahora7.setText("Análisis detallado del rendimiento empresarial");
        jPanel_Reportes.add(lbl_atender_ahora7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        btnGenerarReporte.setBackground(new java.awt.Color(0, 121, 216));
        btnGenerarReporte.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnGenerarReporte.setForeground(new java.awt.Color(255, 255, 255));
        btnGenerarReporte.setText("Generar reporte");
        btnGenerarReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarReporteActionPerformed(evt);
            }
        });
        jPanel_Reportes.add(btnGenerarReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 40, 183, 52));

        panel_dis_mensual.setBackground(new java.awt.Color(236, 243, 248));
        jPanel_Reportes.add(panel_dis_mensual, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 290, 430, 430));

        panel_dis_departamento.setBackground(new java.awt.Color(236, 243, 248));
        jPanel_Reportes.add(panel_dis_departamento, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 440, 430));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setText("Asistencia Mensual");
        jPanel_Reportes.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 250, 430, -1));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel16.setText("Distribucion por Departamento");
        jPanel_Reportes.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 430, -1));

        PanelTab.addTab("Reportes", jPanel_Reportes);

        jPanel_Inventario.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_Inventario.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaInventario.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tablaInventario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane8.setViewportView(tablaInventario);

        jPanel_Inventario.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 116, 910, 590));

        lbl_atender_ahora5.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lbl_atender_ahora5.setText("Gestion de inventario");
        jPanel_Inventario.add(lbl_atender_ahora5, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 19, -1, -1));

        lbl_atender_ahora8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbl_atender_ahora8.setText("Administra la información de todo el inventario");
        jPanel_Inventario.add(lbl_atender_ahora8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        jPanel17.setLayout(new java.awt.GridLayout(1, 0, 5, 0));

        btnEliminarProducto.setBackground(new java.awt.Color(255, 51, 51));
        btnEliminarProducto.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnEliminarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarProducto.setText("Eliminar");
        btnEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProductoActionPerformed(evt);
            }
        });
        jPanel17.add(btnEliminarProducto);

        btnEditarProducto.setBackground(new java.awt.Color(255, 153, 102));
        btnEditarProducto.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnEditarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnEditarProducto.setText("Editar");
        btnEditarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarProductoActionPerformed(evt);
            }
        });
        jPanel17.add(btnEditarProducto);

        btnAgregarEmpleado1.setBackground(new java.awt.Color(0, 121, 216));
        btnAgregarEmpleado1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnAgregarEmpleado1.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarEmpleado1.setText("Nuevo producto");
        btnAgregarEmpleado1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarEmpleado1ActionPerformed(evt);
            }
        });
        jPanel17.add(btnAgregarEmpleado1);

        jPanel_Inventario.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 40, -1, 50));

        PanelTab.addTab("Inventario", jPanel_Inventario);

        jPanel_Usuarios.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_Usuarios.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaUsuarios.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tablaUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane9.setViewportView(tablaUsuarios);

        jPanel_Usuarios.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 116, 910, 590));

        lbl_atender_ahora9.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lbl_atender_ahora9.setText("Gestion de usuarios");
        jPanel_Usuarios.add(lbl_atender_ahora9, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 19, -1, -1));

        lbl_atender_ahora10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbl_atender_ahora10.setText("Administra la información de todos los usuarios");
        jPanel_Usuarios.add(lbl_atender_ahora10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        jPanel18.setLayout(new java.awt.GridLayout(1, 0, 5, 0));

        btnEliminarUsuario.setBackground(new java.awt.Color(255, 51, 51));
        btnEliminarUsuario.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnEliminarUsuario.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarUsuario.setText("Eliminar");
        btnEliminarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarUsuarioActionPerformed(evt);
            }
        });
        jPanel18.add(btnEliminarUsuario);

        btnEditarUsuario.setBackground(new java.awt.Color(255, 153, 102));
        btnEditarUsuario.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnEditarUsuario.setForeground(new java.awt.Color(255, 255, 255));
        btnEditarUsuario.setText("Editar");
        btnEditarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarUsuarioActionPerformed(evt);
            }
        });
        jPanel18.add(btnEditarUsuario);

        btnAgregarUsuario.setBackground(new java.awt.Color(0, 121, 216));
        btnAgregarUsuario.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnAgregarUsuario.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarUsuario.setText("Nuevo usuario");
        btnAgregarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarUsuarioActionPerformed(evt);
            }
        });
        jPanel18.add(btnAgregarUsuario);

        jPanel_Usuarios.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 40, 420, 50));

        PanelTab.addTab("Usuarios", jPanel_Usuarios);

        getContentPane().add(PanelTab, new org.netbeans.lib.awtextra.AbsoluteConstraints(217, 0, 940, 773));

        jPanel2.setBackground(new java.awt.Color(236, 243, 248));

        jPanel5.setBackground(new java.awt.Color(236, 243, 248));
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.Y_AXIS));

        btnInicio.setText("Inicio");
        btnInicio.setMaximumSize(new java.awt.Dimension(200, 50));
        btnInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInicioActionPerformed(evt);
            }
        });
        jPanel5.add(btnInicio);

        btnEmpleados.setText("Empleados");
        btnEmpleados.setMaximumSize(new java.awt.Dimension(200, 50));
        btnEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpleadosActionPerformed(evt);
            }
        });
        jPanel5.add(btnEmpleados);

        btnAsistencia.setText("Asistencia");
        btnAsistencia.setMaximumSize(new java.awt.Dimension(200, 50));
        btnAsistencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsistenciaActionPerformed(evt);
            }
        });
        jPanel5.add(btnAsistencia);

        btnReportes.setText("Reportes");
        btnReportes.setMaximumSize(new java.awt.Dimension(200, 50));
        btnReportes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportesActionPerformed(evt);
            }
        });
        jPanel5.add(btnReportes);

        btnInventario.setText("Inventario");
        btnInventario.setMaximumSize(new java.awt.Dimension(200, 50));
        btnInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInventarioActionPerformed(evt);
            }
        });
        jPanel5.add(btnInventario);

        btnUsuario.setText("Usuarios");
        btnUsuario.setMaximumSize(new java.awt.Dimension(200, 50));
        btnUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUsuarioActionPerformed(evt);
            }
        });
        jPanel5.add(btnUsuario);

        jPanel3.setBackground(new java.awt.Color(211, 234, 251));

        nombre_usuario.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        nombre_usuario.setText("nombre_usuario");

        rol_usuario.setText("rol_usuario");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rol_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nombre_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(63, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(nombre_usuario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rol_usuario)
                .addGap(18, 18, 18))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel2.setText("MTD");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Modas Textiles Drago");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 773));

        jMenu1.setText("Sistema");

        btnCerrarSesion.setText("Cerrar sesion");
        btnCerrarSesion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnCerrarSesionMousePressed(evt);
            }
        });
        jMenu1.add(btnCerrarSesion);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInicioActionPerformed
        PanelTab.setSelectedIndex(0);
        util.Utilidades.actualizarColoresBotones(btnInicio, btnInicio, btnEmpleados, btnAsistencia, btnReportes, btnInventario, btnUsuario);
    }//GEN-LAST:event_btnInicioActionPerformed

    private void btnEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpleadosActionPerformed
        PanelTab.setSelectedIndex(1);
        util.Utilidades.actualizarColoresBotones(btnEmpleados, btnEmpleados, btnInicio, btnAsistencia, btnReportes, btnInventario, btnUsuario);
    }//GEN-LAST:event_btnEmpleadosActionPerformed

    private void btnAsistenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsistenciaActionPerformed
        PanelTab.setSelectedIndex(2);
        util.Utilidades.actualizarColoresBotones(btnAsistencia, btnAsistencia, btnInicio, btnEmpleados, btnReportes, btnInventario, btnUsuario);
    }//GEN-LAST:event_btnAsistenciaActionPerformed

    private void btnReportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportesActionPerformed
        PanelTab.setSelectedIndex(3);
        util.Utilidades.actualizarColoresBotones(btnReportes, btnReportes, btnInicio, btnEmpleados, btnAsistencia, btnInventario, btnUsuario);
    }//GEN-LAST:event_btnReportesActionPerformed

    private void btnAgregarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarEmpleadoActionPerformed
        DAgregarEmpleado agregar = new DAgregarEmpleado(this, false, conexionDB);
        agregar.setVisible(true);
    }//GEN-LAST:event_btnAgregarEmpleadoActionPerformed

    private void btnCerrarSesionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCerrarSesionMousePressed
        // Cerrar conexión y ventana actual
        conexionDB.cerrarConexion();
        this.dispose();

        // Crear nueva conexión
        dao.impl.Conexion conexionDBNew = dao.impl.Conexion.getInstance();

        // Volver al login
        Login lg = new Login(conexionDBNew);
        lg.setVisible(true);
    }//GEN-LAST:event_btnCerrarSesionMousePressed

    private void btnGenerarReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarReporteActionPerformed
        DGenerarReporte dgenerarReporte = new DGenerarReporte(this, false, conexionDB);
        dgenerarReporte.setVisible(true);

    }//GEN-LAST:event_btnGenerarReporteActionPerformed

    private void btnInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventarioActionPerformed
        PanelTab.setSelectedIndex(4);
        util.Utilidades.actualizarColoresBotones(btnInventario, btnInventario, btnInicio, btnEmpleados, btnAsistencia, btnReportes, btnUsuario);
    }//GEN-LAST:event_btnInventarioActionPerformed

    private void btnAgregarEmpleado1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarEmpleado1ActionPerformed
        DAgregarInventario agregar = new DAgregarInventario(this, false, conexionDB);
        agregar.setVisible(true);
    }//GEN-LAST:event_btnAgregarEmpleado1ActionPerformed

    private void btnEditarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarProductoActionPerformed
        try {
            int filaSeleccionada = tablaInventario.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un producto para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Obtener datos de la tabla
            int id = (int) tablaInventario.getValueAt(filaSeleccionada, 0);
            String nombre = (String) tablaInventario.getValueAt(filaSeleccionada, 1);
            int stock_actual = (int) tablaInventario.getValueAt(filaSeleccionada, 2);
            String unidad = (String) tablaInventario.getValueAt(filaSeleccionada, 3);
            String ubicacion = (String) tablaInventario.getValueAt(filaSeleccionada, 4);
            int stock_minimo = (int) tablaInventario.getValueAt(filaSeleccionada, 5);

            // Precio unitario: quitar "S/" y convertir
            String precioStr = tablaInventario.getValueAt(filaSeleccionada, 6).toString();

            NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "PE"));
            Number numero = formatoMoneda.parse(precioStr);
            double precio_unitario = numero.doubleValue();
            String estadoStr = (String) tablaInventario.getValueAt(filaSeleccionada, 7);
            boolean estado = estadoStr.equals("Activo");

            // Crear objeto Inventario
            Inventario inventario = new Inventario(id, nombre, stock_actual, unidad, ubicacion, stock_minimo, precio_unitario, estado);

            // Abrir el diálogo en modo edición
            DAgregarInventario dialog = new DAgregarInventario(this, true, conexionDB, inventario);
            dialog.setVisible(true);
        } catch (ParseException ex) {
            System.getLogger(Dashboard.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }//GEN-LAST:event_btnEditarProductoActionPerformed

    private void btnEditarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarEmpleadoActionPerformed
        int filaSeleccionada = tablaEmpleados.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado de la tabla", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idEmpleado = (int) tablaEmpleados.getValueAt(filaSeleccionada, 0);

        EmpleadoController dao = new EmpleadoController(conexionDB);
        Empleado empleado = dao.obtenerEmpleadoPorId(idEmpleado);

        if (empleado != null) {
            DAgregarEmpleado dialog = new DAgregarEmpleado(this, true, conexionDB, empleado);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Empleado no encontrado en la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnEditarEmpleadoActionPerformed

    private void btnEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProductoActionPerformed
        int filaSeleccionada = tablaInventario.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un empleado de la tabla",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener ID del empleado seleccionado
        int idProducto = (int) tablaInventario.getValueAt(filaSeleccionada, 0);

        // Confirmación antes de eliminar
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar al producto con ID " + idProducto + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                InventarioDAOImpl dao = new InventarioDAOImpl(conexionDB.getConexion());
                dao.eliminar(idProducto);

                JOptionPane.showMessageDialog(this,
                        "Producto eliminado con éxito.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

                // Refrescar tabla del Dashboard
                cargarInventarioEnTabla();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar producto: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                logger.log(java.util.logging.Level.SEVERE, "Error al eliminar producto", ex);
            }
        }
    }//GEN-LAST:event_btnEliminarProductoActionPerformed

    private void btnEliminarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarEmpleadoActionPerformed
        int filaSeleccionada = tablaEmpleados.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un empleado de la tabla",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener ID del empleado seleccionado
        int idEmpleado = (int) tablaEmpleados.getValueAt(filaSeleccionada, 0);

        // Confirmación antes de eliminar
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar al empleado con ID " + idEmpleado + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {

            EmpleadoController dao = new EmpleadoController(conexionDB);
            dao.eliminarEmpleado(idEmpleado);

            JOptionPane.showMessageDialog(this,
                    "Empleado eliminado con éxito.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

            // Refrescar tabla del Dashboard
            cargarEmpleadosEnTabla(new EmpleadoController(conexionDB));

        }
    }//GEN-LAST:event_btnEliminarEmpleadoActionPerformed

    private void btnEliminarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarUsuarioActionPerformed

    private void btnEditarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditarUsuarioActionPerformed

    private void btnAgregarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAgregarUsuarioActionPerformed

    private void btnUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUsuarioActionPerformed
        PanelTab.setSelectedIndex(5);
        util.Utilidades.actualizarColoresBotones(btnUsuario, btnUsuario, btnInicio, btnEmpleados, btnAsistencia, btnReportes, btnInventario);
    }//GEN-LAST:event_btnUsuarioActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane PanelTab;
    private javax.swing.JButton btnAgregarEmpleado;
    private javax.swing.JButton btnAgregarEmpleado1;
    private javax.swing.JButton btnAgregarUsuario;
    private javax.swing.JButton btnAsistencia;
    private javax.swing.JMenuItem btnCerrarSesion;
    private javax.swing.JButton btnEditarEmpleado;
    private javax.swing.JButton btnEditarProducto;
    private javax.swing.JButton btnEditarUsuario;
    private javax.swing.JButton btnEliminarEmpleado;
    private javax.swing.JButton btnEliminarProducto;
    private javax.swing.JButton btnEliminarUsuario;
    private javax.swing.JButton btnEmpleados;
    private javax.swing.JButton btnGenerarReporte;
    private javax.swing.JButton btnInicio;
    private javax.swing.JButton btnInventario;
    private javax.swing.JButton btnReportes;
    private javax.swing.JButton btnUsuario;
    private javax.swing.JLabel fecha_actual;
    private javax.swing.JLabel fecha_actual1;
    private javax.swing.JLabel hora_actual;
    private javax.swing.JLabel hora_actual1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanel_Asistencia;
    private javax.swing.JPanel jPanel_Empleados;
    private javax.swing.JPanel jPanel_Inicio;
    private javax.swing.JPanel jPanel_Inventario;
    private javax.swing.JPanel jPanel_Reportes;
    private javax.swing.JPanel jPanel_Usuarios;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JLabel lbl_atender_ahora10;
    private javax.swing.JLabel lbl_atender_ahora3;
    private javax.swing.JLabel lbl_atender_ahora4;
    private javax.swing.JLabel lbl_atender_ahora5;
    private javax.swing.JLabel lbl_atender_ahora6;
    private javax.swing.JLabel lbl_atender_ahora7;
    private javax.swing.JLabel lbl_atender_ahora8;
    private javax.swing.JLabel lbl_atender_ahora9;
    private javax.swing.JLabel lbl_horas_promedio;
    private javax.swing.JLabel lbl_horas_trabajadas;
    private javax.swing.JLabel lbl_horas_trabajadas1;
    private javax.swing.JLabel lbl_presentes_hoy;
    private javax.swing.JLabel lbl_presentes_hoy1;
    private javax.swing.JLabel lbl_presentes_hoy2;
    private javax.swing.JLabel lbl_total_ausentes;
    private javax.swing.JLabel lbl_total_empleados;
    private javax.swing.JLabel lbl_total_empleados2;
    private javax.swing.JLabel nombre_usuario;
    private javax.swing.JScrollPane panel_actividad_reciente;
    private javax.swing.JScrollPane panel_dis_departamento;
    private javax.swing.JScrollPane panel_dis_mensual;
    private javax.swing.JLabel rol_usuario;
    private javax.swing.JTable tablaAsistencia;
    private javax.swing.JTable tablaEmpleados;
    private javax.swing.JTable tablaInventario;
    private javax.swing.JTable tablaUsuarios;
    private javax.swing.JLabel txt_bienvenida;
    private javax.swing.JLabel txt_bienvenida1;
    // End of variables declaration//GEN-END:variables
}
