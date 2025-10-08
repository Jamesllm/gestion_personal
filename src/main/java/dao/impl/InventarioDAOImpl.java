package dao.impl;

import dao.interfaces.IInventarioDAO;
import model.Inventario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventarioDAOImpl implements IInventarioDAO {

    private Connection conn;

    public InventarioDAOImpl(Connection conn) {
        this.conn = conn;
    }

    // Insertar un nuevo item
    @Override
    public boolean insertar(Inventario inv) throws SQLException {
        String sql = "INSERT INTO inventario (nombre_item, stock_actual, unidad, ubicacion, stock_minimo, precio_unitario) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, inv.getNombreItem());
            ps.setInt(2, inv.getStockActual());
            ps.setString(3, inv.getUnidad());
            ps.setString(4, inv.getUbicacion());
            ps.setInt(5, inv.getStockMinimo());
            ps.setDouble(6, inv.getPrecioUnitario());
            return ps.executeUpdate() > 0;
        }
    }

    // Actualizar item existente
    @Override
    public boolean actualizar(Inventario inv) throws SQLException {
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
            return ps.executeUpdate() > 0;
        }
    }

    // Eliminar item
    @Override
    public boolean eliminar(int idItem) throws SQLException {
        String sql = "UPDATE inventario SET estado= FALSE WHERE id_item=?;";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idItem);
            return ps.executeUpdate() > 0;
        }
    }

    // Buscar por ID
    @Override
    public Inventario buscarPorId(int idItem) throws SQLException {
        String sql = "SELECT * FROM inventario WHERE id_item=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idItem);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        }
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
