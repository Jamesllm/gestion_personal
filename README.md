# Documentación Técnica

**Proyecto: Sistema de Control de Asistencia e Inventario**
**Empresa: MODAS TEXTILES DRAGO S.A.C.**
**Curso:** Curso Integrador: Sistema 1
**C��digo:** AC-S08-APF2

---

## Integrantes

* Anghello Aron Tafur Hoyos
* David Mathew Ortiz Salazar
* Jose Martin Shaquihuanga Ayala
* Ronal James Llapapasca Montes
* Junior Danfert Yesquen Ojeda

**Docente:** Anaximandro Fernéndez Guerrero
**Sección:** 11722
**A?o:** 2025

---

## Introducción

La empresa **MODAS TEXTILES DRAGO S.A.C.** enfrenta problemas derivados del **registro manual de asistencia** y el **control básico de inventario**. Esto genera errores, inconsistencias y pérdida de tiempo.

El presente proyecto plantea un **aplicativo de escritorio** con **Java Swing** y **PostgreSQL**, que permitirá optimizar estos procesos, mejorando la eficiencia operativa y reduciendo errores humanos.

---

## Misión

Ofrecer una experiencia excepcional en moda, combinando dise?o, tecnología y sostenibilidad, entregando prendas modernas con impacto positivo en el planeta.

## Visión

Ser líderes en la moda peruana, donde la innovación y el respeto por la naturaleza se entrelacen para definir la moda del ma?ana.

---

## Alcance del Proyecto

El sistema permitirá:

* Registrar entrada y salida de los trabajadores.
* Generar reportes de asistencia, tardanzas y ausencias.
* Definir roles de usuario (administrador, supervisor y empleado).
* Gestionar el inventario de materiales.
* Exportar reportes en formatos de consulta.
* Garantizar seguridad y robustez mediante PostgreSQL.

---

##  Objetivos

### Objetivo General

Dise?ar y desarrollar un aplicativo de escritorio que optimice el **control del personal e inventario** en la empresa Drago.

### Objetivos Específicos

* Crear una **interfaz amigable y fácil de usar**.
* Implementar una **base de datos robusta en PostgreSQL**.
* Definir **perfiles de usuario** con roles y permisos diferenciados.
* Ejecutar **pruebas exhaustivas** para validar el correcto funcionamiento.

---

##  Análisis del Contexto

* **Problema actual:** registros manuales en hojas físicas y Excel.
* **Consecuencias:** errores en asistencia e inventario, dificultad para consolidar información, reportes poco confiables.
* **Solución propuesta:** sistema de escritorio con BD centralizada, digitalización de procesos y reportes automáticos.

---

##  Arquitectura del Sistema

### Lenguajes y Herramientas

* **Frontend/Interfaz:** Java Swing
* **Backend/DAO:** Java con patrán DAO (Data Access Object)
* **Base de Datos:** PostgreSQL
* **Patrones de Dise?o:** Singleton (Conexión DB), MVC (modelo-vista-controlador)
* **Reportes:** JasperReports


##  Componentes Técnicos

### 1. **Conexión a la Base de Datos (Singleton)**

```java
public class Conexion {
    private static Conexion instancia;
    private Connection conexion;
    private final String URL = "jdbc:postgresql://localhost:5432/sistema_drago";
    private final String USUARIO = "gestion";
    private final String CLAVE = "123456";

    private Conexion() {
        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Conexion getInstance() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    public Connection getConexion() { return conexion; }
    public void cerrarConexion() { instancia = null; }
}
```

---

### 2. **Módulo de Inventario**

* Tabla `inventario`:

```sql
CREATE TABLE inventario (
    id_item SERIAL PRIMARY KEY,
    nombre_item VARCHAR(200) NOT NULL,
    stock_actual INTEGER DEFAULT 0,
    unidad VARCHAR(20) NOT NULL,
    ubicacion VARCHAR(100),
    stock_minimo INTEGER DEFAULT 0,
    precio_unitario NUMERIC(10,2),
    estado BOOLEAN DEFAULT true,
    fecha_ultimo_movimiento TIMESTAMP DEFAULT NOW()
);
```

* Renderizado en tabla con formato en soles:

```java
NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "PE"));
fila[6] = formatoMoneda.format(inv.getPrecioUnitario());
fila[7] = inv.isEstado() ? "Activo" : "Inactivo";
```

---

### 3. **Módulo de Asistencia**

* Registro de entrada:

```java
public void registrarEntrada(Asistencia asistencia) throws SQLException {
    String sql = "INSERT INTO asistencia (id_empleado, hora_entrada, estado) VALUES (?, ?, ?)";
    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
        ps.setInt(1, asistencia.getIdEmpleado());
        ps.setTime(2, Time.valueOf(asistencia.getHoraEntrada()));
        ps.setString(3, asistencia.getEstado());
        ps.executeUpdate();
    }
}
```

* Registro de salida:

```java
public void registrarSalida(int idAsistencia, LocalTime horaSalida, String estado) throws SQLException {
    String sql = "UPDATE asistencia SET hora_salida = ?, estado = ? WHERE id_asistencia = ?";
    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
        ps.setTime(1, Time.valueOf(horaSalida));
        ps.setString(2, estado);
        ps.setInt(3, idAsistencia);
        ps.executeUpdate();
    }
}
```

---

### 4. **Login y Roles**

* Validación de credenciales:

  * Verifica campos vacíos.
  * Valida usuario y contrase?a en BD (con hash).
  * Incrementa intentos fallidos y bloquea tras 3 errores.
  * Verifica **rol = Administrador** antes de acceder al panel.

* Flujo visual:
  ![Proceso de Login](./login-flow.png)

---

### 5. **Reportes**

* JasperReports configurado para:

  * Reportes de asistencia diaria/mensual.
  * Reportes de inventario y stock mínimo.
* Exportación a **PDF y Excel**.

---

##  Gestión del Proyecto

### Project Charter

* **Título:** Sistema de Control de Asistencia del Personal
* **Jefe de Proyecto:** José Shaquihuanga
* **Sponsor:** Corporación Grupo Drago S.A.C.
* **Presupuesto:** S/. 5,000
* **Cronograma:** Inicio 12/08/2025 - Fin 05/09/2025

### Lean Canvas

*(Incluido en el entregable del curso con el análisis de problema, cliente y propuesta de valor)*

---

## Riesgos Identificados

* Resistencia del personal al cambio digital.
* Problemas técnicos con hardware de asistencia.
* Dependencia de conexión estable a internet.
* Posible dificultad en integración con otros sistemas de la empresa.

---

##  Costos Estimados

* Desarrollo: S/. 3,000
* Licencias y librerías: S/. 1,000
* Capacitación y soporte: S/. 1,000
  **Total: S/. 5,000**

---

##  Conclusiones

El desarrollo del sistema permitirá a la empresa:

* Reducir errores en la asistencia y el inventario.
* Agilizar la generación de reportes.
* Mejorar el control de personal.
* Optimizar recursos para aumentar competitividad.