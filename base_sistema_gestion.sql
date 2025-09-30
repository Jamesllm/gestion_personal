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
    precio_unitario DECIMAL(10,2) DEFAULT 0.00
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
('María Elena', 'Rodríguez Silva', '23456789', 2, '2024-02-01', 'mariaelena@gmail.com', '935487895'),
('Carlos Alberto', 'López Mendoza', '34567890', 3, '2024-01-20', 'carlosmendoza@gmail.com', '954789654'),
('Ana Sofía', 'Martínez Vega', '45678901', 4, '2024-03-01', 'anavega@gmail.com', '935489574'),
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
('$2a$10$i1HdlFd8QHiTdv7kp0lLVePfZZUpGSiOD6X7HKBJxxbkIt8ZxmS3e', 3, 6), -- password: hello
('$2a$10$i1HdlFd8QHiTdv7kp0lLVePfZZUpGSiOD6X7HKBJxxbkIt8ZxmS3e', 3, 7); -- password: hello

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