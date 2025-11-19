package dao;

import model.*;
import util.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VentaDAO {

    // ============================
    // REGISTRAR VENTA (ya lo tenías)
    // ============================
    public boolean registrarVenta(Venta v, DetalleVenta d) {
        String sqlVenta = "INSERT INTO ventas(usuario_id, tipo_venta, turno_id, total, activo) VALUES (?,?,?,?,1)";
        String sqlDet   = "INSERT INTO detalle_venta(venta_id, producto_id, tipo_copia, cantidad, precio_unitario, subtotal) " +
                          "VALUES (?,?,?,?,?,?)";

        Connection con = null;
        try {
            con = Conexion.getConnection();
            con.setAutoCommit(false);

            PreparedStatement psVenta = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
            psVenta.setInt(1, v.getUsuario().getId());
            psVenta.setString(2, v.getTipoVenta());
            if (v.getTurno() != null && v.getTurno().getId() > 0) {
                psVenta.setInt(3, v.getTurno().getId());
            } else {
                psVenta.setNull(3, Types.INTEGER);
            }
            psVenta.setDouble(4, v.getTotal());
            psVenta.executeUpdate();

            ResultSet rs = psVenta.getGeneratedKeys();
            int ventaId = 0;
            if (rs.next()) ventaId = rs.getInt(1);

            PreparedStatement psDet = con.prepareStatement(sqlDet);
            psDet.setInt(1, ventaId);
            if (d.getProducto() != null) {
                psDet.setInt(2, d.getProducto().getId());
            } else {
                psDet.setNull(2, Types.INTEGER);
            }
            psDet.setString(3, d.getTipoCopia());
            psDet.setInt(4, d.getCantidad());
            psDet.setDouble(5, d.getPrecioUnitario());
            psDet.setDouble(6, d.getSubtotal());
            psDet.executeUpdate();

            con.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            if (con != null) try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        } finally {
            if (con != null) try { con.setAutoCommit(true); con.close(); } catch (SQLException ex) {}
        }
        return false;
    }

    // ============================
    // LISTAR POR FECHA (reportes)
    // ============================
    public List<Venta> listarPorFecha(Date desde, Date hasta) {
        List<Venta> lista = new ArrayList<>();

        String sql = "SELECT v.*, u.nombre AS usuario_nombre, t.nombre AS turno_nombre " +
                     "FROM ventas v " +
                     "JOIN usuarios u ON v.usuario_id = u.id " +
                     "LEFT JOIN turnos t ON v.turno_id = t.id " +
                     "WHERE v.fecha_hora BETWEEN ? AND ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, new java.sql.Timestamp(desde.getTime()));
            ps.setTimestamp(2, new java.sql.Timestamp(hasta.getTime()));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Venta v = new Venta();
                Usuario u = new Usuario();
                Turno t = new Turno();

                v.setId(rs.getInt("id"));
                v.setFechaHora(rs.getTimestamp("fecha_hora"));
                v.setTipoVenta(rs.getString("tipo_venta"));
                v.setTotal(rs.getDouble("total"));
                v.setActivo(rs.getBoolean("activo"));

                u.setId(rs.getInt("usuario_id"));
                u.setNombre(rs.getString("usuario_nombre"));
                v.setUsuario(u);

                if (rs.getObject("turno_id") != null) {
                    t.setId(rs.getInt("turno_id"));
                    t.setNombre(rs.getString("turno_nombre"));
                    v.setTurno(t);
                }

                lista.add(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // ============================
    // ANULAR (historial)
    // ============================
    public boolean anular(int idVenta) {
        String sql = "UPDATE ventas SET activo=0 WHERE id=?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idVenta);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ============================
    // TOTAL POR USUARIO Y TURNO (ya lo tenías)
    // ============================
    public double totalPorUsuarioYTurnoHoy(int usuarioId, int turnoId) {
        String sql = "SELECT SUM(total) AS total FROM ventas " +
                     "WHERE usuario_id=? AND turno_id=? AND DATE(fecha_hora)=CURDATE() AND activo=1";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ps.setInt(2, turnoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("total");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // ============================
    // ➕ NUEVO: TOTAL GLOBAL DEL TURNO (TODOS LOS USUARIOS HOY)
    // ============================
    public double totalGlobalPorTurnoHoy(int turnoId) {
        String sql = "SELECT SUM(total) AS total FROM ventas " +
                     "WHERE turno_id=? AND DATE(fecha_hora)=CURDATE() AND activo=1";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, turnoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // ============================
    // ➕ NUEVO: LISTAR VENTAS DEL TURNO HOY (TODOS LOS USUARIOS)
    // ============================
    public List<Venta> listarPorTurnoHoy(int turnoId) {
        List<Venta> lista = new ArrayList<>();

        String sql = "SELECT v.*, u.nombre AS usuario_nombre " +
                     "FROM ventas v " +
                     "JOIN usuarios u ON v.usuario_id = u.id " +
                     "WHERE v.turno_id = ? AND DATE(v.fecha_hora) = CURDATE() AND v.activo = 1 " +
                     "ORDER BY v.fecha_hora DESC";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, turnoId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Venta v = new Venta();
                Usuario u = new Usuario();

                v.setId(rs.getInt("id"));
                v.setFechaHora(rs.getTimestamp("fecha_hora"));
                v.setTipoVenta(rs.getString("tipo_venta"));
                v.setTotal(rs.getDouble("total"));
                v.setActivo(rs.getBoolean("activo"));

                u.setId(rs.getInt("usuario_id"));
                u.setNombre(rs.getString("usuario_nombre"));
                v.setUsuario(u);

                lista.add(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // ============================
    // (Ya tenías) listarTodas / listarPorUsuario si las usas en historial
    // ============================
    public List<Venta> listarTodas() {
    List<Venta> lista = new ArrayList<>();

    String sql = "SELECT v.*, " +
                 "u.nombre AS usuario_nombre, u.usuario AS usuario_login, " +
                 "t.id AS turno_id, t.nombre AS turno_nombre " +
                 "FROM ventas v " +
                 "JOIN usuarios u ON v.usuario_id = u.id " +
                 "LEFT JOIN turnos t ON v.turno_id = t.id " +
                 "ORDER BY v.fecha_hora DESC";

    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Venta v = new Venta();
            Usuario u = new Usuario();

            // Campos de venta
            v.setId(rs.getInt("id"));
            v.setFechaHora(rs.getTimestamp("fecha_hora"));
            v.setTipoVenta(rs.getString("tipo_venta"));
            v.setTotal(rs.getDouble("total"));
            v.setActivo(rs.getBoolean("activo"));

            // Usuario
            u.setId(rs.getInt("usuario_id"));
            u.setNombre(rs.getString("usuario_nombre"));
            u.setUsuario(rs.getString("usuario_login"));
            v.setUsuario(u);

            // Turno (puede ser null)
            Object turnoObj = rs.getObject("turno_id");
            if (turnoObj != null) {
                Turno t = new Turno();
                t.setId(rs.getInt("turno_id"));
                t.setNombre(rs.getString("turno_nombre"));
                v.setTurno(t);
            } else {
                v.setTurno(null);
            }

            lista.add(v);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return lista;
}


   public List<Venta> listarPorUsuario(int usuarioId) {
    List<Venta> lista = new ArrayList<>();

    String sql = "SELECT v.*, " +
                 "u.nombre AS usuario_nombre, u.usuario AS usuario_login, " +
                 "t.id AS turno_id, t.nombre AS turno_nombre " +
                 "FROM ventas v " +
                 "JOIN usuarios u ON v.usuario_id = u.id " +
                 "LEFT JOIN turnos t ON v.turno_id = t.id " +
                 "WHERE v.usuario_id = ? " +
                 "ORDER BY v.fecha_hora DESC";

    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, usuarioId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Venta v = new Venta();
            Usuario u = new Usuario();

            // Venta
            v.setId(rs.getInt("id"));
            v.setFechaHora(rs.getTimestamp("fecha_hora"));
            v.setTipoVenta(rs.getString("tipo_venta"));
            v.setTotal(rs.getDouble("total"));
            v.setActivo(rs.getBoolean("activo"));

            // Usuario
            u.setId(rs.getInt("usuario_id"));
            u.setNombre(rs.getString("usuario_nombre"));
            u.setUsuario(rs.getString("usuario_login"));
            v.setUsuario(u);

            // Turno
            Object turnoObj = rs.getObject("turno_id");
            if (turnoObj != null) {
                Turno t = new Turno();
                t.setId(rs.getInt("turno_id"));
                t.setNombre(rs.getString("turno_nombre"));
                v.setTurno(t);
            } else {
                v.setTurno(null);
            }

            lista.add(v);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return lista;
}

}