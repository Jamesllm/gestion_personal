# Documentaci車n T谷cnica

**Proyecto: Sistema de Control de Asistencia e Inventario**
**Empresa: MODAS TEXTILES DRAGO S.A.C.**
**Curso:** Curso Integrador: Sistema 1
**C車digo:** AC-S08-APF2

---

## Integrantes

* Anghello Aron Tafur Hoyos
* David Mathew Ortiz Salazar
* Jose Martin Shaquihuanga Ayala
* Ronal James Llapapasca Montes
* Junior Danfert Yesquen Ojeda

**Docente:** Anaximandro Fern芍ndez Guerrero
**Secci車n:** 11722
**A?o:** 2025

---

## Introducci車n

La empresa **MODAS TEXTILES DRAGO S.A.C.** enfrenta problemas derivados del **registro manual de asistencia** y el **control b芍sico de inventario**. Esto genera errores, inconsistencias y p谷rdida de tiempo.

El presente proyecto plantea un **aplicativo de escritorio** con **Java Swing** y **PostgreSQL**, que permitir芍 optimizar estos procesos, mejorando la eficiencia operativa y reduciendo errores humanos.

---

## Misi車n

Ofrecer una experiencia excepcional en moda, combinando dise?o, tecnolog赤a y sostenibilidad, entregando prendas modernas con impacto positivo en el planeta.

## Visi車n

Ser l赤deres en la moda peruana, donde la innovaci車n y el respeto por la naturaleza se entrelacen para definir la moda del ma?ana.

---

## Alcance del Proyecto

El sistema permitir芍:

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

### Objetivos Espec赤ficos

* Crear una **interfaz amigable y f芍cil de usar**.
* Implementar una **base de datos robusta en PostgreSQL**.
* Definir **perfiles de usuario** con roles y permisos diferenciados.
* Ejecutar **pruebas exhaustivas** para validar el correcto funcionamiento.

---

##  An芍lisis del Contexto

* **Problema actual:** registros manuales en hojas f赤sicas y Excel.
* **Consecuencias:** errores en asistencia e inventario, dificultad para consolidar informaci車n, reportes poco confiables.
* **Soluci車n propuesta:** sistema de escritorio con BD centralizada, digitalizaci車n de procesos y reportes autom芍ticos.

---

##  Arquitectura del Sistema

### Lenguajes y Herramientas

* **Frontend/Interfaz:** Java Swing
* **Backend/DAO:** Java con patr車n DAO (Data Access Object)
* **Base de Datos:** PostgreSQL
* **Patrones de Dise?o:** Singleton (Conexi車n DB), MVC (modelo-vista-controlador)
* **Reportes:** JasperReports

### Diagrama de Capas

```
[ Interfaz Gr芍fica (Swing) ]
          ∣
[ Controladores ]
          ∣
[ DAO (InventarioDAO, AsistenciaDAO, UsuarioDAO) ]
          ∣
[ Base de Datos PostgreSQL ]
```

---

##  Componentes T谷cnicos

### 1. **Conexi車n a la Base de Datos (Singleton)**

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

### 2. **M車dulo de Inventario**

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

### 3. **M車dulo de Asistencia**

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

* Validaci車n de credenciales:

  * Verifica campos vac赤os.
  * Valida usuario y contrase?a en BD (con hash).
  * Incrementa intentos fallidos y bloquea tras 3 errores.
  * Verifica **rol = Administrador** antes de acceder al panel.

* Flujo visual:
  ![Proceso de Login](./login-flow.png)

---

### 5. **Reportes**

* JasperReports configurado para:

  * Reportes de asistencia diaria/mensual.
  * Reportes de inventario y stock m赤nimo.
* Exportaci車n a **PDF y Excel**.

---

##  Gesti車n del Proyecto

### Project Charter

* **T赤tulo:** Sistema de Control de Asistencia del Personal
* **Jefe de Proyecto:** Jos谷 Shaquihuanga
* **Sponsor:** Corporaci車n Grupo Drago S.A.C.
* **Presupuesto:** S/. 5,000
* **Cronograma:** Inicio 12/08/2025 每 Fin 05/09/2025

### Lean Canvas

*(Incluido en el entregable del curso con el an芍lisis de problema, cliente y propuesta de valor)*

---

## Riesgos Identificados

* Resistencia del personal al cambio digital.
* Problemas t谷cnicos con hardware de asistencia.
* Dependencia de conexi車n estable a internet.
* Posible dificultad en integraci車n con otros sistemas de la empresa.

---

##  Costos Estimados

* Desarrollo: S/. 3,000
* Licencias y librer赤as: S/. 1,000
* Capacitaci車n y soporte: S/. 1,000
  **Total: S/. 5,000**

---

##  Conclusiones

El desarrollo del sistema permitir芍 a la empresa:

* Reducir errores en la asistencia y el inventario.
* Agilizar la generaci車n de reportes.
* Mejorar el control de personal.
* Optimizar recursos para aumentar competitividad.

---

 **Anexos:** Diagramas UML, ERD de la base de datos, Gantt Chart, Mockups de pantallas, Lean Canvas completo.

---
