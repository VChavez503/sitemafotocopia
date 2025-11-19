package dao;

import model.Precio;
import util.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrecioDAO {

    private Precio map(ResultSet rs) throws SQLException {
        Precio p = new Precio();
        p.setId(rs.getInt("id"));
        p.setTipo(rs.getString("tipo"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPrecio(rs.getDouble("precio"));
        p.setActivo(rs.getBoolean("activo"));
        return p;
    }

    public List<Precio> listar() {
        List<Precio> lista = new ArrayList<>();
        String sql = "SELECT * FROM precios ORDER BY tipo";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(map(rs));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al listar precios: " + e.getMessage());
        }
        return lista;
    }
    
    public Precio buscarPorId(int id) {
    String sql = "SELECT * FROM precios WHERE id = ?";

    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Precio p = new Precio();
            p.setId(rs.getInt("id"));
            p.setTipo(rs.getString("tipo"));
            p.setDescripcion(rs.getString("descripcion"));
            p.setPrecio(rs.getDouble("precio"));
            p.setActivo(rs.getBoolean("activo"));
            return p;
        }

    } catch (SQLException e) {
        System.out.println("❌ Error al buscar precio por id: " + e.getMessage());
    }
    return null;
}


    public Precio buscarPorTipo(String tipo) {
        String sql = "SELECT * FROM precios " +
                     "WHERE UPPER(TRIM(tipo)) = UPPER(TRIM(?)) " +
                     "AND activo = 1";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.println("🔎 [PrecioDAO.buscarPorTipo] buscando tipo = " + tipo);

            ps.setString(1, tipo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Precio p = map(rs);
                    System.out.println("✅ Encontrado precio: " + p.getTipo() + " = " + p.getPrecio());
                    return p;
                } else {
                    System.out.println("⚠ No se encontró precio para tipo = " + tipo);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al buscar precio por tipo: " + e.getMessage());
        }
        return null;
    }

    public boolean insertar(Precio p) {
        String sql = "INSERT INTO precios(tipo, descripcion, precio, activo) VALUES (?,?,?,?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getTipo());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPrecio());
            ps.setBoolean(4, p.isActivo());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al insertar precio: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizar(Precio p) {
        String sql = "UPDATE precios SET tipo=?, descripcion=?, precio=?, activo=? WHERE id=?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getTipo());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPrecio());
            ps.setBoolean(4, p.isActivo());
            ps.setInt(5, p.getId());

            System.out.println("🛠 [PrecioDAO.actualizar] id=" + p.getId() +
                               ", tipo=" + p.getTipo() +
                               ", precio=" + p.getPrecio());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar precio: " + e.getMessage());
        }
        return false;
    }
    public boolean eliminar(int id) {
    String sql = "DELETE FROM precios WHERE id = ?";

    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id);
        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        System.out.println("❌ Error al eliminar precio: " + e.getMessage());
    }
    return false;
}

}