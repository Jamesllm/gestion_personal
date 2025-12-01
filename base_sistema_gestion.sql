-- =====================================================
-- CREACIÓN DE TABLAS
-- =====================================================

-- Tabla Departamento
CREATE TABLE departamento (
    id_departamento SERIAL PRIMARY KEY,
    nombre_departamento VARCHAR(100) NOT NULL UNIQUE
);

-- Tabla Empleado
CREATE TABLE empleado (
    id_empleado SERIAL PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    dni VARCHAR(8) NOT NULL UNIQUE,
    id_departamento INTEGER NOT NULL,
    fecha_contratacion DATE DEFAULT CURRENT_DATE,
    correo_electronico VARCHAR(150) UNIQUE,
    telefono VARCHAR(20),
    estado BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_departamento) REFERENCES departamento(id_departamento)
);

-- Tabla Rol
CREATE TABLE rol (
    id_rol SERIAL PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(200)
);

-- Tabla Usuario
CREATE TABLE usuario (
    id_usuario SERIAL PRIMARY KEY,
    contrasena VARCHAR(255) NOT NULL,
    id_rol INTEGER NOT NULL,
    id_empleado INTEGER NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_rol) REFERENCES rol(id_rol),
    FOREIGN KEY (id_empleado) REFERENCES empleado(id_empleado)
);

-- Tabla Asistencia
CREATE TABLE asistencia (
    id_asistencia SERIAL PRIMARY KEY,
    id_empleado INTEGER NOT NULL,
    fecha DATE NOT NULL DEFAULT CURRENT_DATE,
    hora_entrada TIME,
    hora_salida TIME,
    estado VARCHAR(20) DEFAULT 'PRESENTE',
    observaciones VARCHAR(500),
    FOREIGN KEY (id_empleado) REFERENCES empleado(id_empleado),
    UNIQUE(id_empleado, fecha)
);

-- Tabla Reporte
CREATE TABLE reporte (
    id_reporte SERIAL PRIMARY KEY,
    nombre_reporte VARCHAR(200) NOT NULL,
    fecha_generacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_usuario_generador INTEGER NOT NULL,
    tipo_reporte VARCHAR(50),
    ruta_archivo VARCHAR(500),
    FOREIGN KEY (id_usuario_generador) REFERENCES usuario(id_usuario)
);

-- Tabla TipoComprobante
CREATE TABLE tipo_comprobante (
    id_tipo_comprobante SERIAL PRIMARY KEY,
    nombre_tipo VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(200)
);

-- Tabla Inventario
CREATE TABLE inventario (
    id_item SERIAL PRIMARY KEY,
    nombre_item VARCHAR(200) NOT NULL,
    stock_actual INTEGER DEFAULT 0,
    unidad VARCHAR(20) NOT NULL,
    ubicacion VARCHAR(100),
    fecha_ultimo_movimiento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    stock_minimo INTEGER DEFAULT 0,
    precio_unitario DECIMAL(10,2) DEFAULT 0.00,
	estado BOOLEAN DEFAULT TRUE
);

-- Tabla MovimientoInventario
CREATE TABLE movimiento_inventario (
    id_movimiento SERIAL PRIMARY KEY,
    id_item INTEGER NOT NULL,
    tipo_movimiento VARCHAR(20) NOT NULL CHECK (tipo_movimiento IN ('ENTRADA', 'SALIDA')),
    cantidad INTEGER NOT NULL,
    fecha_movimiento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_usuario_responsable INTEGER NOT NULL,
    id_tipo_comprobante INTEGER,
    numero_comprobante VARCHAR(50),
    observaciones VARCHAR(500),
    FOREIGN KEY (id_item) REFERENCES inventario(id_item),
    FOREIGN KEY (id_usuario_responsable) REFERENCES usuario(id_usuario),
    FOREIGN KEY (id_tipo_comprobante) REFERENCES tipo_comprobante(id_tipo_comprobante)
);

CREATE TABLE modulo (
    id_modulo SERIAL PRIMARY KEY,
    nombre_modulo VARCHAR(50) NOT NULL
);

CREATE TABLE rol_modulo (
    id_rol INT REFERENCES rol(id_rol),
    id_modulo INT REFERENCES modulo(id_modulo),
    activo BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id_rol, id_modulo)
);


ALTER TABLE usuario 
ADD COLUMN codigo_temporal VARCHAR(10),
ADD COLUMN expira_codigo TIMESTAMP;

ALTER TABLE usuario ADD COLUMN cambiar_password BOOLEAN DEFAULT TRUE;


-- =====================================================
-- INSERCIÓN DE DATOS DE PRUEBA
-- =====================================================

-- Insertar Departamentos
INSERT INTO departamento (nombre_departamento) VALUES
('Administración'),
('Producción'),
('Ventas'),
('Diseño'),
('Logística'),
('Recursos Humanos'),
('Contabilidad'),
('Marketing');

-- Insertar Empleados
INSERT INTO empleado (nombres, apellidos, dni, id_departamento, fecha_contratacion, correo_electronico, telefono) VALUES
('Ronal', 'Llapapasca Montes', '12345678', 1, '2024-01-15', 'ronallapapasca@gmail.com', '987456321'),
('David', 'Ortiz Salazar', '23456789', 2, '2024-02-01', 'davidortiz@gmail.com', '935487895'),
('Martin', 'Shaquihuanca', '34567890', 3, '2024-01-20', 'martinshaqui@gmail.com', '954789654'),
('Anghello', 'Tafur', '45678901', 4, '2024-03-01', 'anghellotafur@gmail.com', '935489574'),
('Luis Fernando', 'González Ruiz', '56789012', 5, '2024-01-10', 'luisruiz@gmail.com', '965874585'),
('Carmen Rosa', 'Hernández Torres', '67890123', 6, '2024-02-15', 'carmentorres@gmail.com', '925478965'),
('Roberto Miguel', 'Díaz Flores', '78901234', 7, '2024-01-25', 'robertomiguel@gmail.com', '923458765'),
('Patricia Luz', 'Morales Castro', '89012345', 8, '2024-03-10', 'patriciacastro@gmail.com', '982648957'),
('David Alejandro', 'Vargas Jiménez', '90123456', 2, '2024-02-20', 'davidjimenes@gmail.com', '988654824'),
('Sandra Milena', 'Ramos Delgado', '01234567', 3, '2024-01-30', 'milenaramos@gmail.com', '912345678');

-- Insertar Roles
INSERT INTO rol (nombre_rol, descripcion) VALUES
('Administrador', 'Acceso completo al sistema'),
('Supervisor', 'Acceso a módulos de gestión y reportes'),
('Empleado', 'Acceso básico para registro de asistencia');

-- Insertar Usuarios
INSERT INTO usuario (contrasena, id_rol, id_empleado) VALUES
('$2a$10$L2J7QBsQtn50jvsbxCO/U.v..n7MNEn8WsWqbweQMFp3wWvxN1ZjK', 1, 1), -- password: admin
('$2a$10$i1HdlFd8QHiTdv7kp0lLVePfZZUpGSiOD6X7HKBJxxbkIt8ZxmS3e', 2, 2), -- password: hello
('$2a$10$i1HdlFd8QHiTdv7kp0lLVePfZZUpGSiOD6X7HKBJxxbkIt8ZxmS3e', 3, 3), -- password: hello
('$2a$10$i1HdlFd8QHiTdv7kp0lLVePfZZUpGSiOD6X7HKBJxxbkIt8ZxmS3e', 3, 4), -- password: hello
('$2a$10$i1HdlFd8QHiTdv7kp0lLVePfZZUpGSiOD6X7HKBJxxbkIt8ZxmS3e', 3, 5); -- password: hello

-- Insertar Asistencias de prueba (últimas 2 semanas)
INSERT INTO asistencia (id_empleado, fecha, hora_entrada, hora_salida, estado) VALUES
-- Semana actual
(1, CURRENT_DATE, '08:00:00', '17:00:00', 'PRESENTE'),
(2, CURRENT_DATE, '08:15:00', '17:30:00', 'TARDANZA'),
(3, CURRENT_DATE, '08:00:00', '17:00:00', 'PRESENTE'),
(4, CURRENT_DATE, '09:00:00', '18:00:00', 'PRESENTE'),
(5, CURRENT_DATE, '08:30:00', '17:30:00', 'PRESENTE'),
-- Día anterior
(1, CURRENT_DATE - INTERVAL '1 day', '08:05:00', '17:05:00', 'PRESENTE'),
(2, CURRENT_DATE - INTERVAL '1 day', '08:00:00', '17:00:00', 'PRESENTE'),
(3, CURRENT_DATE - INTERVAL '1 day', NULL, NULL, 'FALTA'),
(4, CURRENT_DATE - INTERVAL '1 day', '08:10:00', '17:10:00', 'PRESENTE'),
(5, CURRENT_DATE - INTERVAL '1 day', '08:45:00', '17:45:00', 'TARDANZA'),
-- Hace 2 días
(1, CURRENT_DATE - INTERVAL '2 days', '08:00:00', '17:00:00', 'PRESENTE'),
(2, CURRENT_DATE - INTERVAL '2 days', '08:20:00', '17:20:00', 'TARDANZA'),
(3, CURRENT_DATE - INTERVAL '2 days', '08:00:00', '17:00:00', 'PRESENTE'),
(4, CURRENT_DATE - INTERVAL '2 days', '08:00:00', '17:00:00', 'PRESENTE'),
(5, CURRENT_DATE - INTERVAL '2 days', '08:15:00', '17:15:00', 'PRESENTE');

-- Insertar Tipos de Comprobante
INSERT INTO tipo_comprobante (nombre_tipo, descripcion) VALUES
('Factura', 'Comprobante de pago por compra'),
('Boleta', 'Comprobante de venta'),
('Guía de Remisión', 'Documento de traslado de mercancías'),
('Orden de Compra', 'Documento interno de solicitud'),
('Vale de Salida', 'Documento interno de salida de materiales'),
('Nota de Ingreso', 'Documento de ingreso de materiales');

-- Insertar Items de Inventario
INSERT INTO inventario (nombre_item, stock_actual, unidad, ubicacion, stock_minimo, precio_unitario) VALUES
('Tela Algodón Blanco', 500, 'metros', 'Almacén A - Estante 1', 50, 12.50),
('Hilo Poliéster Negro', 200, 'rollos', 'Almacén A - Estante 2', 20, 8.75),
('Botones Plásticos', 5000, 'unidades', 'Almacén B - Cajón 1', 500, 0.15),
('Cremalleras 20cm', 300, 'unidades', 'Almacén B - Cajón 2', 30, 2.50),
('Etiquetas Marca', 1000, 'unidades', 'Almacén C - Estante 1', 100, 0.25),
('Tela Denim Azul', 250, 'metros', 'Almacén A - Estante 3', 25, 18.00),
('Agujas Industriales', 100, 'unidades', 'Taller - Caja Herramientas', 10, 5.50),
('Tijeras Industriales', 15, 'unidades', 'Taller - Mesa 1', 2, 45.00),
('Máquina de Coser Overlock', 5, 'unidades', 'Taller - Área Producción', 1, 2500.00),
('Plancha Industrial', 3, 'unidades', 'Taller - Área Acabado', 1, 850.00);

-- Insertar Movimientos de Inventario
INSERT INTO movimiento_inventario (id_item, tipo_movimiento, cantidad, id_usuario_responsable, id_tipo_comprobante, numero_comprobante, observaciones) VALUES
-- Entradas
(1, 'ENTRADA', 100, 1, 1, 'F001-0001', 'Compra quincenal de tela algodón'),
(2, 'ENTRADA', 50, 1, 1, 'F001-0002', 'Reposición de hilo poliéster'),
(3, 'ENTRADA', 1000, 2, 4, 'OC-2025-001', 'Pedido mensual de botones'),
-- Salidas
(1, 'SALIDA', 80, 2, 5, 'VS-001', 'Para producción camisas blancas'),
(2, 'SALIDA', 15, 3, 5, 'VS-002', 'Para orden de producción #123'),
(3, 'SALIDA', 200, 2, 5, 'VS-003', 'Producción pantalones ejecutivos'),
(4, 'SALIDA', 25, 3, 5, 'VS-004', 'Reparación de chaquetas'),
-- Más entradas
(6, 'ENTRADA', 75, 1, 1, 'F001-0003', 'Compra tela denim para nueva línea'),
(7, 'ENTRADA', 20, 1, 2, 'B001-0001', 'Reposición agujas de repuesto'),
(8, 'SALIDA', 2, 2, 5, 'VS-005', 'Transferencia a taller secundario');

-- Insertar Reportes de ejemplo
INSERT INTO reporte (nombre_reporte, id_usuario_generador, tipo_reporte, ruta_archivo) VALUES
('Reporte Asistencia Mensual - Enero 2025', 1, 'ASISTENCIA', '/reportes/asistencia_enero_2025.pdf'),
('Reporte Tardanzas por Departamento', 2, 'ASISTENCIA', '/reportes/tardanzas_depto_2025.pdf'),
('Inventario - Stock Crítico', 1, 'INVENTARIO', '/reportes/stock_critico_2025.pdf'),
('Movimientos Inventario - Último Mes', 2, 'INVENTARIO', '/reportes/movimientos_mes_2025.pdf'),
('Reporte General de Empleados', 1, 'EMPLEADOS', '/reportes/empleados_general_2025.pdf');


-- Insertar modulo
INSERT INTO modulo (nombre_modulo) VALUES
('Inicio'),
('Empleados'),
('Asistencia'),
('Reportes'),
('Inventario');

-- Administrador (todo activo)
INSERT INTO rol_modulo (id_rol, id_modulo, activo)
SELECT 1, id_modulo, TRUE FROM modulo;

-- Supervisor (todo activo también, pero con restricciones en la app)
INSERT INTO rol_modulo (id_rol, id_modulo, activo)
SELECT 2, id_modulo, TRUE FROM modulo;

-- Empleado (solo inicio y asistencia)
INSERT INTO rol_modulo (id_rol, id_modulo, activo)
VALUES
(3, 1, TRUE),  -- Inicio
(3, 3, TRUE);  -- Asistencia

-- =====================================================
-- ÍNDICES PARA MEJORAR RENDIMIENTO
-- =====================================================

CREATE INDEX idx_empleado_dni ON empleado(dni);
CREATE INDEX idx_asistencia_fecha ON asistencia(fecha);
CREATE INDEX idx_asistencia_empleado_fecha ON asistencia(id_empleado, fecha);
CREATE INDEX idx_movimiento_fecha ON movimiento_inventario(fecha_movimiento);
CREATE INDEX idx_inventario_stock ON inventario(stock_actual);


-- =====================================================
-- CONSULTAS DE VERIFICACIÓN
-- =====================================================

-- Ver empleados por departamento
-- SELECT d.nombre_departamento, COUNT(e.id_empleado) as total_empleados
-- FROM departamento d
-- LEFT JOIN empleado e ON d.id_departamento = e.id_departamento
-- GROUP BY d.id_departamento, d.nombre_departamento
-- ORDER BY d.nombre_departamento;

-- Ver asistencias de hoy
-- SELECT e.nombres, e.apellidos, a.hora_entrada, a.hora_salida, a.estado
-- FROM asistencia a
-- JOIN empleado e ON a.id_empleado = e.id_empleado
-- WHERE a.fecha = CURRENT_DATE
-- ORDER BY a.hora_entrada;

-- Ver items con stock crítico
-- SELECT nombre_item, stock_actual, stock_minimo
-- FROM inventario
-- WHERE stock_actual <= stock_minimo
-- ORDER BY stock_actual;


-- =====================================================
-- PROCEDIMIENTOS ALMACENADOS
-- =====================================================

-- =====================================================
-- MODULOS
CREATE OR REPLACE FUNCTION sp_obtener_modulos_por_rol(p_id_rol INT)
RETURNS TABLE(
    id_modulo INT,
    nombre_modulo VARCHAR,
    activo BOOLEAN
) AS $$
BEGIN
    RETURN QUERY
    SELECT m.id_modulo, m.nombre_modulo, rm.activo
    FROM rol_modulo rm
    JOIN modulo m ON rm.id_modulo = m.id_modulo
    WHERE rm.id_rol = p_id_rol AND rm.activo = TRUE
    ORDER BY m.id_modulo;
END;
$$ LANGUAGE plpgsql;

-- =====================================================
-- LOGIN SERVICE
CREATE OR REPLACE FUNCTION sp_autenticar_usuario(p_dni VARCHAR)
RETURNS TABLE(
    id_usuario INT,
    nombre_completo VARCHAR,
    contrasena VARCHAR,
    id_rol INT,
    nombre_rol VARCHAR,
    id_empleado INT
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        u.id_usuario,
        CAST(e.nombres || ' ' || e.apellidos AS VARCHAR) AS nombre_completo,
        u.contrasena,
        u.id_rol,
        r.nombre_rol,
        u.id_empleado
    FROM usuario u
    JOIN empleado e ON u.id_empleado = e.id_empleado
    JOIN rol r ON u.id_rol = r.id_rol
    WHERE e.dni = p_dni
      AND u.activo = TRUE;
END;
$$ LANGUAGE plpgsql;

-- =====================================================
--- USUARIOS
-- LOGIN USUARIO
CREATE OR REPLACE FUNCTION sp_login_usuario(p_username VARCHAR, p_password VARCHAR)
RETURNS TABLE (
    id_usuario INT,
    username VARCHAR,
    contrasena VARCHAR,
    cambiar_password BOOLEAN,
    id_empleado INT,
    id_rol INT,
    nombre_rol VARCHAR
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        u.id_usuario,
        e.nombres AS username,
        u.contrasena,
        u.cambiar_password,
        u.id_empleado,
        r.id_rol,
        r.nombre_rol
    FROM usuario u
    JOIN rol r ON u.id_rol = r.id_rol
    JOIN empleado e ON u.id_empleado = e.id_empleado
    WHERE e.nombres = p_username 
      AND u.contrasena = p_password
      AND u.activo = TRUE;
END;
$$ LANGUAGE plpgsql;


-- INSERTAR USUARIO
CREATE OR REPLACE FUNCTION sp_insertar_usuario(
    p_contrasena VARCHAR,
    p_id_rol INT,
    p_id_empleado INT,
    p_cambiar_password BOOLEAN
)
RETURNS BOOLEAN AS $$
BEGIN
    INSERT INTO usuario (contrasena, id_rol, id_empleado, cambiar_password)
    VALUES (p_contrasena, p_id_rol, p_id_empleado, p_cambiar_password);
    RETURN TRUE;
EXCEPTION WHEN OTHERS THEN
    RETURN FALSE;
END;
$$ LANGUAGE plpgsql;


-- ACTUALIZAR USUARIO
CREATE OR REPLACE FUNCTION sp_actualizar_usuario(
    p_id_usuario INT,
    p_contrasena VARCHAR,
    p_id_rol INT,
    p_id_empleado INT,
    p_cambiar_password BOOLEAN
)
RETURNS BOOLEAN AS $$
BEGIN
    UPDATE usuario
    SET contrasena = p_contrasena,
        id_rol = p_id_rol,
        id_empleado = p_id_empleado,
        cambiar_password = p_cambiar_password
    WHERE id_usuario = p_id_usuario;
    RETURN FOUND;
END;
$$ LANGUAGE plpgsql;


-- ELIMINAR USUARIO (desactivar)
CREATE OR REPLACE FUNCTION sp_eliminar_usuario(p_id_usuario INT)
RETURNS BOOLEAN AS $$
BEGIN
    UPDATE usuario SET activo = FALSE WHERE id_usuario = p_id_usuario;
    RETURN FOUND;
END;
$$ LANGUAGE plpgsql;


-- CAMBIAR CONTRASEÑA
CREATE OR REPLACE FUNCTION sp_cambiar_password(p_id_usuario INT, p_nueva_contrasena VARCHAR)
RETURNS BOOLEAN AS $$
BEGIN
    UPDATE usuario SET contrasena = p_nueva_contrasena, cambiar_password = FALSE
    WHERE id_usuario = p_id_usuario;
    RETURN FOUND;
END;
$$ LANGUAGE plpgsql;


-- LISTAR TODOS
CREATE OR REPLACE FUNCTION sp_listar_usuarios()
RETURNS TABLE (
    id_usuario INT,
    username VARCHAR,
    contrasena VARCHAR,
    cambiar_password BOOLEAN,
    id_rol INT,
    nombre_rol VARCHAR,
    id_empleado INT,
    activo BOOLEAN
) AS $$
BEGIN
    RETURN QUERY
    SELECT u.id_usuario, e.nombres AS username, u.contrasena, u.cambiar_password,
           r.id_rol, r.nombre_rol, u.id_empleado, u.activo
    FROM usuario u
    JOIN rol r ON u.id_rol = r.id_rol
    JOIN empleado e ON u.id_empleado = e.id_empleado
    ORDER BY u.id_usuario DESC;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_obtener_usuario_por_id(p_id_usuario INTEGER)
RETURNS TABLE (
    id_usuario INTEGER,
    username VARCHAR,
    contrasena VARCHAR,
    cambiar_password BOOLEAN,
    id_rol INTEGER,
    nombre_rol VARCHAR,
    id_empleado INTEGER,
    activo BOOLEAN
)
AS $$
BEGIN
    RETURN QUERY
    SELECT 
        u.id_usuario,
        e.nombres AS username,
        u.contrasena,
        u.cambiar_password,
        r.id_rol,
        r.nombre_rol,
        u.id_empleado,
        u.activo
    FROM usuario u
    JOIN rol r ON u.id_rol = r.id_rol
    JOIN empleado e ON u.id_empleado = e.id_empleado
    WHERE u.id_usuario = p_id_usuario;
END;
$$ LANGUAGE plpgsql;

-- =====================================================
--- DEPARTAMENTO

-- Crear Departamento
CREATE OR REPLACE FUNCTION sp_crear_departamento(
    p_nombre_departamento VARCHAR,
    p_estado BOOLEAN
)
RETURNS VOID
AS $$
BEGIN
    INSERT INTO departamento (nombre_departamento, estado)
    VALUES (p_nombre_departamento, p_estado);
END;
$$ LANGUAGE plpgsql;

-- Obtener todos los Departamentos
CREATE OR REPLACE FUNCTION sp_obtener_todos_departamentos()
RETURNS TABLE (
    id_departamento INTEGER,
    nombre_departamento VARCHAR,
    estado BOOLEAN
)
AS $$
BEGIN
    RETURN QUERY
    SELECT id_departamento, nombre_departamento, estado
    FROM departamento
    ORDER BY id_departamento;
END;
$$ LANGUAGE plpgsql;

-- Obtener Departamento por ID
CREATE OR REPLACE FUNCTION sp_obtener_departamento_por_id(p_id_departamento INTEGER)
RETURNS TABLE (
    id_departamento INTEGER,
    nombre_departamento VARCHAR,
    estado BOOLEAN
)
AS $$
BEGIN
    RETURN QUERY
    SELECT id_departamento, nombre_departamento, estado
    FROM departamento
    WHERE id_departamento = p_id_departamento;
END;
$$ LANGUAGE plpgsql;

-- Actualizar Departamento
CREATE OR REPLACE FUNCTION sp_actualizar_departamento(
    p_id_departamento INTEGER,
    p_nombre_departamento VARCHAR,
    p_estado BOOLEAN
)
RETURNS VOID
AS $$
BEGIN
    UPDATE departamento
    SET nombre_departamento = p_nombre_departamento,
        estado = p_estado
    WHERE id_departamento = p_id_departamento;
END;
$$ LANGUAGE plpgsql;

-- Eliminar Departamento
CREATE OR REPLACE FUNCTION sp_eliminar_departamento(p_id_departamento INTEGER)
RETURNS VOID
AS $$
BEGIN
    DELETE FROM departamento WHERE id_departamento = p_id_departamento;
END;
$$ LANGUAGE plpgsql;

-- Distribución de Empleados por Departamento
CREATE OR REPLACE FUNCTION sp_distribucion_departamentos()
RETURNS TABLE (
    departamento VARCHAR,
    cantidad_empleados INTEGER
)
AS $$
BEGIN
    RETURN QUERY
    SELECT d.nombre_departamento AS departamento, COUNT(e.id_empleado) AS cantidad_empleados
    FROM empleado e
    INNER JOIN departamento d ON e.id_departamento = d.id_departamento
    GROUP BY d.nombre_departamento
    ORDER BY d.nombre_departamento;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_insertar_inventario(
    p_nombre_item VARCHAR,
    p_stock_actual INTEGER,
    p_unidad VARCHAR,
    p_ubicacion VARCHAR,
    p_stock_minimo INTEGER,
    p_precio_unitario DECIMAL
) RETURNS BOOLEAN AS $$
BEGIN
    INSERT INTO inventario(nombre_item, stock_actual, unidad, ubicacion, stock_minimo, precio_unitario)
    VALUES (p_nombre_item, p_stock_actual, p_unidad, p_ubicacion, p_stock_minimo, p_precio_unitario);

    RETURN TRUE;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_actualizar_inventario(
    p_id_item INTEGER,
    p_nombre_item VARCHAR,
    p_stock_actual INTEGER,
    p_unidad VARCHAR,
    p_ubicacion VARCHAR,
    p_stock_minimo INTEGER,
    p_precio_unitario DECIMAL
) RETURNS BOOLEAN AS $$
BEGIN
    UPDATE inventario 
    SET nombre_item = p_nombre_item,
        stock_actual = p_stock_actual,
        unidad = p_unidad,
        ubicacion = p_ubicacion,
        stock_minimo = p_stock_minimo,
        precio_unitario = p_precio_unitario,
        fecha_ultimo_movimiento = NOW()
    WHERE id_item = p_id_item;

    RETURN FOUND; -- TRUE si actualizó filas
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_eliminar_inventario(
    p_id_item INTEGER
) RETURNS BOOLEAN AS $$
BEGIN
    UPDATE inventario 
    SET estado = FALSE
    WHERE id_item = p_id_item;

    RETURN FOUND;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_buscar_inventario(p_id_item INTEGER)
RETURNS SETOF inventario AS $$
BEGIN
    RETURN QUERY SELECT * FROM inventario WHERE id_item = p_id_item;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_listar_inventario()
RETURNS SETOF inventario AS $$
BEGIN
    RETURN QUERY SELECT * FROM inventario ORDER BY id_item DESC;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_crear_empleado(
    p_nombres VARCHAR,
    p_apellidos VARCHAR,
    p_dni VARCHAR,
    p_fecha_contratacion DATE,
    p_correo VARCHAR,
    p_telefono VARCHAR,
    p_id_departamento INT
) RETURNS BOOLEAN AS $$
BEGIN
    INSERT INTO empleado(nombres, apellidos, dni, fecha_contratacion, correo_electronico, telefono, id_departamento)
    VALUES (p_nombres, p_apellidos, p_dni, p_fecha_contratacion, p_correo, p_telefono, p_id_departamento);

    RETURN TRUE;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_listar_empleados()
RETURNS SETOF empleado AS $$
BEGIN
    RETURN QUERY SELECT * FROM empleado ORDER BY id_empleado DESC;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_obtener_empleado_por_id(p_id INT)
RETURNS SETOF empleado AS $$
BEGIN
    RETURN QUERY SELECT * FROM empleado WHERE id_empleado = p_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_actualizar_empleado(
    p_id INT,
    p_nombres VARCHAR,
    p_apellidos VARCHAR,
    p_dni VARCHAR,
    p_fecha DATE,
    p_correo VARCHAR,
    p_telefono VARCHAR,
    p_id_departamento INT,
    p_estado BOOLEAN
) RETURNS BOOLEAN AS $$
BEGIN
    UPDATE empleado
    SET nombres = p_nombres,
        apellidos = p_apellidos,
        dni = p_dni,
        fecha_contratacion = p_fecha,
        correo_electronico = p_correo,
        telefono = p_telefono,
        id_departamento = p_id_departamento,
        estado = p_estado
    WHERE id_empleado = p_id;

    RETURN FOUND;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_eliminar_empleado(p_id INT)
RETURNS BOOLEAN AS $$
BEGIN
    UPDATE empleado SET estado = FALSE WHERE id_empleado = p_id;
    RETURN FOUND;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_total_empleados()
RETURNS INT AS $$
DECLARE total INT;
BEGIN
    SELECT COUNT(*) INTO total FROM empleado;
    RETURN total;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_total_presentes_hoy()
RETURNS INT AS $$
DECLARE total INT;
BEGIN
    SELECT COUNT(*)
    INTO total
    FROM asistencia
    WHERE fecha = CURRENT_DATE AND estado = 'PRESENTE';

    RETURN total;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_horas_totales_hoy()
RETURNS NUMERIC AS $$
DECLARE horas NUMERIC;
BEGIN
    SELECT SUM(EXTRACT(EPOCH FROM (hora_salida - hora_entrada)) / 3600)
    INTO horas
    FROM asistencia
    WHERE fecha = CURRENT_DATE
    AND hora_entrada IS NOT NULL
    AND hora_salida IS NOT NULL;

    RETURN COALESCE(horas, 0);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_promedio_horas_hoy()
RETURNS NUMERIC AS $$
DECLARE promedio NUMERIC;
BEGIN
    SELECT AVG(EXTRACT(EPOCH FROM (hora_salida - hora_entrada)) / 3600)
    INTO promedio
    FROM asistencia
    WHERE fecha = CURRENT_DATE
      AND hora_entrada IS NOT NULL
      AND hora_salida IS NOT NULL;

    RETURN COALESCE(promedio, 0);
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION sp_total_ausentes_hoy()
RETURNS INT AS $$
DECLARE total INT;
BEGIN
    SELECT COUNT(*) INTO total
    FROM empleado
    WHERE id_empleado NOT IN (
        SELECT id_empleado FROM asistencia WHERE fecha = CURRENT_DATE
    );

    RETURN total;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_actividad_reciente(p_limite INT)
RETURNS TABLE (
    id_asistencia INT,
    empleado TEXT,
    fecha DATE,
    hora_entrada TIME,
    hora_salida TIME,
    estado TEXT
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        a.id_asistencia,
        e.nombres || ' ' || e.apellidos AS empleado,
        a.fecha,
        a.hora_entrada,
        a.hora_salida,
        CASE 
            WHEN a.hora_salida IS NULL THEN 'En oficina'
            ELSE 'Finalizado'
        END
    FROM asistencia a
    JOIN empleado e ON e.id_empleado = a.id_empleado
    ORDER BY a.fecha DESC, a.hora_entrada DESC
    LIMIT p_limite;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_distribucion_departamento()
RETURNS TABLE (
    departamento TEXT,
    cantidad INT
) AS $$
BEGIN
    RETURN QUERY
    SELECT d.nombre_departamento, COUNT(e.id_empleado)
    FROM departamento d
    LEFT JOIN empleado e ON d.id_departamento = e.id_departamento
    GROUP BY d.nombre_departamento
    ORDER BY COUNT(e.id_empleado) DESC;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_asistencia_mensual()
RETURNS TABLE (
    mes TEXT,
    cantidad INT
) AS $$
BEGIN
    RETURN QUERY
    SELECT TO_CHAR(fecha, 'MM'), COUNT(*)
    FROM asistencia
    GROUP BY TO_CHAR(fecha, 'MM')
    ORDER BY 1;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_obtener_id_por_dni(p_dni VARCHAR)
RETURNS INT AS $$
DECLARE resultado INT;
BEGIN
    SELECT id_empleado INTO resultado FROM empleado WHERE dni = p_dni;
    RETURN COALESCE(resultado, -1);
END;
$$ LANGUAGE plpgsql;
