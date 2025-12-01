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
        String sql = "{? = call sp_insertar_inventario(?, ?, ?, ?, ?, ?)}";

        try (CallableStatement cs = conn.prepareCall(sql)) {
            cs.registerOutParameter(1, Types.BOOLEAN);
            cs.setString(2, inv.getNombreItem());
            cs.setInt(3, inv.getStockActual());
            cs.setString(4, inv.getUnidad());
            cs.setString(5, inv.getUbicacion());
            cs.setInt(6, inv.getStockMinimo());
            cs.setDouble(7, inv.getPrecioUnitario());

            cs.execute();
            return cs.getBoolean(1);
        }
    }

    // Actualizar item existente
    @Override
    public boolean actualizar(Inventario inv) throws SQLException {
        String sql = "{? = call sp_actualizar_inventario(?, ?, ?, ?, ?, ?)}";

        try (CallableStatement cs = conn.prepareCall(sql)) {
            cs.registerOutParameter(1, Types.BOOLEAN);
            cs.setInt(2, inv.getIdItem());
            cs.setString(3, inv.getNombreItem());
            cs.setInt(4, inv.getStockActual());
            cs.setString(5, inv.getUnidad());
            cs.setString(6, inv.getUbicacion());
            cs.setInt(7, inv.getStockMinimo());
            cs.setDouble(8, inv.getPrecioUnitario());

            cs.execute();
            return cs.getBoolean(1);
        }
    }

    // Eliminar item (desactivar)
    @Override
    public boolean eliminar(int idItem) throws SQLException {
        String sql = "{? = call sp_eliminar_inventario(?)}";

        try (CallableStatement cs = conn.prepareCall(sql)) {
            cs.registerOutParameter(1, Types.BOOLEAN);
            cs.setInt(2, idItem);
            cs.execute();
            return cs.getBoolean(1);
        }
    }

    // Buscar por ID
    @Override
    public Inventario buscarPorId(int idItem) throws SQLException {
        String sql = "SELECT * FROM sp_buscar_inventario(?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idItem);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }
            return null;
        }
    }

    // Listar todos los items
    @Override
    public List<Inventario> listar() throws SQLException {
        String sql = "SELECT * FROM sp_listar_inventario()";
        List<Inventario> lista = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }
        }
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
