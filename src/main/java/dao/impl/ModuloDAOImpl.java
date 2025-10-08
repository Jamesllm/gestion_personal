package dao.impl;

import dao.interfaces.IModuloDAO;
import model.Modulo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModuloDAOImpl implements IModuloDAO {

    private Connection conexion;

    public ModuloDAOImpl(Connection conexion) {
        this.conexion = conexion;
    }

    // Obtener módulos activos por rol
    @Override
    public List<Modulo> obtenerModulosPorRol(int idRol) throws SQLException {
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
        }
        return modulos;
    }
}
