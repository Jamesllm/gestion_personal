package dao.impl;

import dao.interfaces.IInventarioDAO;
import model.Inventario;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventarioDAOImpl implements IInventarioDAO {

    private static final Logger logger = LoggerFactory.getLogger(InventarioDAOImpl.class);
    private final Connection conn;

    public InventarioDAOImpl(Connection conn) {
        this.conn = Preconditions.checkNotNull(conn, "La conexión no puede ser nula");
    }

    // Insertar un nuevo item
    @Override
    public boolean insertar(Inventario inv) throws SQLException {
        Preconditions.checkNotNull(inv, "El inventario no puede ser nulo");

        String sql = "INSERT INTO inventario (nombre_item, stock_actual, unidad, ubicacion, stock_minimo, precio_unitario) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, inv.getNombreItem());
            ps.setInt(2, inv.getStockActual());
            ps.setString(3, inv.getUnidad());
            ps.setString(4, inv.getUbicacion());
            ps.setInt(5, inv.getStockMinimo());
            ps.setDouble(6, inv.getPrecioUnitario());

            boolean resultado = ps.executeUpdate() > 0;
            if (resultado) {
                logger.info("✅ Item '{}' insertado correctamente en inventario.", inv.getNombreItem());
            }
            return resultado;

        } catch (SQLException e) {
            logger.error("❌ Error al insertar el item '{}': {}", inv.getNombreItem(), e.getMessage());
            throw e;
        }
    }

    // Actualizar item existente
    @Override
    public boolean actualizar(Inventario inv) throws SQLException {
        Preconditions.checkNotNull(inv, "El inventario no puede ser nulo");
        Preconditions.checkArgument(inv.getIdItem() > 0, "El ID del item debe ser válido");

        String sql = "UPDATE inventario SET nombre_item=?, stock_actual=?, unidad=?, ubicacion=?, "
                + "stock_minimo=?, precio_unitario=?, fecha_ultimo_movimiento=NOW() WHERE id_item=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, inv.getNombreItem());
            ps.setInt(2, inv.getStockActual());
            ps.setString(3, inv.getUnidad());
            ps.setString(4, inv.getUbicacion());
            ps.setInt(5, inv.getStockMinimo());
            ps.setDouble(6, inv.getPrecioUnitario());
            ps.setInt(7, inv.getIdItem());

            boolean resultado = ps.executeUpdate() > 0;
            if (resultado) {
                logger.info("🔄 Item '{}' actualizado correctamente.", inv.getNombreItem());
            }
            return resultado;

        } catch (SQLException e) {
            logger.error("❌ Error al actualizar el item '{}': {}", inv.getNombreItem(), e.getMessage());
            throw e;
        }
    }

    // Eliminar item (desactivar)
    @Override
    public boolean eliminar(int idItem) throws SQLException {
        Preconditions.checkArgument(idItem > 0, "El ID del item debe ser mayor que 0");

        String sql = "UPDATE inventario SET estado = FALSE WHERE id_item = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idItem);
            boolean resultado = ps.executeUpdate() > 0;

            if (resultado) {
                logger.warn("⚠️ Item con ID {} marcado como inactivo.", idItem);
            }
            return resultado;

        } catch (SQLException e) {
            logger.error("❌ Error al eliminar (desactivar) el item con ID {}: {}", idItem, e.getMessage());
            throw e;
        }
    }

    // Buscar por ID
    @Override
    public Inventario buscarPorId(int idItem) throws SQLException {
        Preconditions.checkArgument(idItem > 0, "El ID debe ser mayor que 0");

        String sql = "SELECT * FROM inventario WHERE id_item = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idItem);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Inventario inv = mapResultSet(rs);
                logger.debug("🔍 Item encontrado: {}", inv.getNombreItem());
                return inv;
            }
        }
        logger.info("ℹ️ No se encontró item con ID {}", idItem);
        return null;
    }

    // Listar todos los items
    @Override
    public List<Inventario> listar() throws SQLException {
        List<Inventario> lista = new ArrayList<>();
        String sql = "SELECT * FROM inventario ORDER BY id_item DESC";

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }
        }

        logger.info("📦 Se listaron {} items de inventario.", lista.size());
        return lista;
    }

    // Mapear ResultSet a objeto
    @Override
    public Inventario mapResultSet(ResultSet rs) throws SQLException {
        Inventario inv = new Inventario();
        inv.setIdItem(rs.getInt("id_item"));
        inv.setNombreItem(rs.getString("nombre_item"));
        inv.setStockActual(rs.getInt("stock_actual"));
        inv.setUnidad(rs.getString("unidad"));
        inv.setUbicacion(rs.getString("ubicacion"));
        inv.setFechaUltimoMovimiento(rs.getTimestamp("fecha_ultimo_movimiento"));
        inv.setStockMinimo(rs.getInt("stock_minimo"));
        inv.setPrecioUnitario(rs.getDouble("precio_unitario"));
        inv.setEstado(rs.getBoolean("estado"));
        return inv;
    }
}
