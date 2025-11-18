package dao;

import model.Archivo;
import model.Usuario;
import util.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArchivoDAO {

    // ---------------------------------------------
    // Mapear una fila de ResultSet a objeto Archivo
    // ---------------------------------------------
    private Archivo mapear(ResultSet rs) throws SQLException {
        Archivo a = new Archivo();

        a.setId(rs.getInt("id"));
        a.setCodigo(rs.getString("codigo"));
        a.setMensaje(rs.getString("mensaje")); // puede venir null
        a.setNombreArchivo(rs.getString("nombre_archivo"));
        a.setRutaArchivo(rs.getString("ruta_archivo"));
        a.setFechaSubida(rs.getTimestamp("fecha_subida"));
        a.setProcesado(rs.getBoolean("procesado"));

        int usuarioId = rs.getInt("usuario_id");

        if (!rs.wasNull()) {
            Usuario u = new Usuario();
            u.setId(usuarioId);

            try { u.setNombre(rs.getString("usuario_nombre")); } catch (SQLException ignore) {}
            try { u.setUsuario(rs.getString("usuario_login")); } catch (SQLException ignore) {}

            a.setUsuario(u);
        }

        return a;
    }

    // ---------------------------------------------
    // INSERT
    // ---------------------------------------------
    public boolean insertar(Archivo a) {

        String sql = "INSERT INTO archivos(usuario_id, codigo, mensaje, nombre_archivo, ruta_archivo, procesado) " +
                     "VALUES (?,?,?,?,?,?)";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = Conexion.getConnection();

            if (con == null) {
                System.out.println("❌ ArchivoDAO.insertar: Conexion.getConnection() devolvió NULL");
                return false;
            }

            ps = con.prepareStatement(sql);

            if (a.getUsuario() != null && a.getUsuario().getId() > 0) {
                ps.setInt(1, a.getUsuario().getId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            ps.setString(2, a.getCodigo());
            ps.setString(3, a.getMensaje()); // MENSAJE del cliente
            ps.setString(4, a.getNombreArchivo());
            ps.setString(5, a.getRutaArchivo());
            ps.setBoolean(6, a.isProcesado());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error al insertar archivo: " + e.getMessage());
        } finally {

            if (ps != null) {
                try { ps.close(); } catch (SQLException ignore) {}
            }

            if (con != null) {
                try { con.close(); } catch (SQLException ignore) {}
            }
        }
        return false;
    }

    // ---------------------------------------------
    // LISTAR POR CÓDIGO
    // ---------------------------------------------
    public List<Archivo> listarPorCodigo(String codigo) {
        List<Archivo> lista = new ArrayList<>();

        String sql = "SELECT a.*, u.nombre AS usuario_nombre, u.usuario AS usuario_login " +
                     "FROM archivos a " +
                     "LEFT JOIN usuarios u ON a.usuario_id = u.id " +
                     "WHERE a.codigo = ? " +
                     "ORDER BY a.fecha_subida DESC";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al listar archivos por código: " + e.getMessage());
        }
        return lista;
    }

    // ---------------------------------------------
    // LISTAR TODOS (bandeja)
    // ---------------------------------------------
    public List<Archivo> listarTodos() {
        List<Archivo> lista = new ArrayList<>();

        String sql = "SELECT a.*, u.nombre AS usuario_nombre, u.usuario AS usuario_login " +
                     "FROM archivos a " +
                     "LEFT JOIN usuarios u ON a.usuario_id = u.id " +
                     "ORDER BY a.fecha_subida DESC";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al listar todos los archivos: " + e.getMessage());
        }
        return lista;
    }
    
    // ---------------------------------------------
    // LISTAR POR USUARIO (para que el cliente vea sus envíos)
    // ---------------------------------------------
    public List<Archivo> listarPorUsuario(int usuarioId) {
        List<Archivo> lista = new ArrayList<>();

        String sql = "SELECT a.*, u.nombre AS usuario_nombre, u.usuario AS usuario_login "
                + "FROM archivos a "
                + "LEFT JOIN usuarios u ON a.usuario_id = u.id "
                + "WHERE a.usuario_id = ? "
                + "ORDER BY a.fecha_subida DESC";

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al listar archivos por usuario: " + e.getMessage());
        }
        return lista;
    }


    // ---------------------------------------------
    // (Opcional) Marcar como procesado
    // ---------------------------------------------
    public boolean marcarProcesado(int idArchivo) {

        String sql = "UPDATE archivos SET procesado = 1 WHERE id = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idArchivo);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error al marcar archivo como procesado: " + e.getMessage());
        }
        return false;
    }
}
