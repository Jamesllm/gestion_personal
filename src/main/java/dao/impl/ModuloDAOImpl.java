package dao.impl;

import com.google.common.base.Preconditions;
import dao.interfaces.IModuloDAO;
import model.Modulo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la interfaz {@link IModuloDAO}.
 * <p>
 * Esta clase se encarga de realizar las operaciones relacionadas con la entidad
 * {@link Modulo} en la base de datos, utilizando JDBC y siguiendo las buenas
 * prácticas de manejo de conexiones.
 * </p>
 *
 * <p>
 * Utiliza {@link org.slf4j.Logger} para registrar información sobre el
 * proceso.</p>
 *
 * @author Ronal
 * @version 1.0
 * @since 2025
 */
public class ModuloDAOImpl implements IModuloDAO {

    private static final Logger logger = LoggerFactory.getLogger(ModuloDAOImpl.class);
    private final Connection conexion;

    /**
     * Constructor que inicializa la conexión a la base de datos.
     *
     * @param conexion conexión activa a la base de datos (no puede ser nula)
     * @throws NullPointerException si la conexión es nula
     */
    public ModuloDAOImpl(Connection conexion) {
        this.conexion = Preconditions.checkNotNull(conexion, "La conexión no puede ser nula");
    }

    /**
     * Obtiene una lista de módulos activos asociados a un rol específico.
     *
     * @param idRol identificador único del rol
     * @return una lista de objetos {@link Modulo} activos asociados al rol
     * @throws IllegalArgumentException si el ID del rol es menor o igual a 0
     * @throws SQLException si ocurre un error al ejecutar la consulta SQL
     */
    @Override
    public List<Modulo> obtenerModulosPorRol(int idRol) throws SQLException {
        Preconditions.checkArgument(idRol > 0, "El ID del rol debe ser mayor a 0");

        List<Modulo> modulos = new ArrayList<>();

        String sql = "{ CALL sp_obtener_modulos_por_rol(?) }";

        try (CallableStatement cs = conexion.prepareCall(sql)) {
            cs.setInt(1, idRol);

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    modulos.add(new Modulo(
                            rs.getInt("id_modulo"),
                            rs.getString("nombre_modulo"),
                            rs.getBoolean("activo")
                    ));
                }
            }

            logger.info("✅ Se obtuvieron {} módulos activos para el rol con ID {}", modulos.size(), idRol);
            return modulos;

        } catch (SQLException e) {
            logger.error("❌ Error al ejecutar el procedimiento almacenado para el rol {}: {}", idRol, e.getMessage());
            throw e;
        }
    }
}
