package dao.impl;

import com.google.common.base.Preconditions;
import dao.interfaces.IModuloDAO;
import model.Modulo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModuloDAOImpl implements IModuloDAO {

    private static final Logger logger = LoggerFactory.getLogger(ModuloDAOImpl.class);
    private final Connection conexion;

    public ModuloDAOImpl(Connection conexion) {
        this.conexion = Preconditions.checkNotNull(conexion, "La conexión no puede ser nula");
    }

    // Obtener módulos activos por rol
    @Override
    public List<Modulo> obtenerModulosPorRol(int idRol) throws SQLException {
        Preconditions.checkArgument(idRol > 0, "El ID del rol debe ser mayor a 0");

        List<Modulo> modulos = new ArrayList<>();
        String sql = """
            SELECT m.id_modulo, m.nombre_modulo, rm.activo
            FROM rol_modulo rm
            JOIN modulo m ON rm.id_modulo = m.id_modulo
            WHERE rm.id_rol = ? AND rm.activo = TRUE
            ORDER BY m.id_modulo
        """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idRol);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    modulos.add(new Modulo(
                            rs.getInt("id_modulo"),
                            rs.getString("nombre_modulo"),
                            rs.getBoolean("activo")
                    ));
                }
            }

            logger.info("Se obtuvieron {} módulos activos para el rol con ID {}", modulos.size(), idRol);
        } catch (SQLException e) {
            logger.error("Error al obtener módulos para rol {}: {}", idRol, e.getMessage(), e);
            throw e;
        }

        return modulos;
    }
}
