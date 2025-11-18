package dao;

import model.Rol;
import model.Usuario;
import util.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // 🔹 Mapea una fila del ResultSet a un objeto Usuario (con su Rol)
    private Usuario mapUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNombre(rs.getString("nombre"));
        u.setCorreo(rs.getString("correo"));
        u.setUsuario(rs.getString("usuario"));
        u.setContrasena(rs.getString("contrasena"));
        u.setActivo(rs.getBoolean("activo"));

        Rol r = new Rol();
        r.setId(rs.getInt("rol_id"));
        try {
            // alias rol_nombre cuando venga del JOIN
            r.setNombre(rs.getString("rol_nombre"));
        } catch (SQLException e) {
            // si no viene el alias, no pasa nada
        }
        u.setRol(r);

        return u;
    }

    // 🔹 LOGIN: busca usuario por usuario + contraseña y que esté activo
    public Usuario login(String usuario, String contrasena) {
        String sql = "SELECT u.*, r.nombre AS rol_nombre " +
                     "FROM usuarios u " +
                     "JOIN roles r ON u.rol_id = r.id " +
                     "WHERE u.usuario = ? AND u.contrasena = ? AND u.activo = 1";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ ERROR SQL en login: " + e.getMessage());
        }
        return null;
    }

    // 🔹 REGISTRAR NUEVO USUARIO
    public boolean registrar(Usuario u) {
        String sql = "INSERT INTO usuarios(nombre, correo, usuario, contrasena, rol_id, activo) " +
                     "VALUES (?,?,?,?,?,1)";

        System.out.println("INTENTANDO REGISTRAR USUARIO EN BD:");
        System.out.println("nombre     = " + u.getNombre());
        System.out.println("correo     = " + u.getCorreo());
        System.out.println("usuario    = " + u.getUsuario());
        System.out.println("contrasena = " + u.getContrasena());
        System.out.println("rol_id     = " + (u.getRol() != null ? u.getRol().getId() : null));

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getCorreo());
            ps.setString(3, u.getUsuario());
            ps.setString(4, u.getContrasena());
            ps.setInt(5, u.getRol().getId());

            int filas = ps.executeUpdate();
            System.out.println("Filas insertadas: " + filas);
            return filas > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("❌ ERROR DE RESTRICCIÓN (UNIQUE o FOREIGN KEY): " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("❌ ERROR SQL al registrar usuario: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ ERROR GENERAL al registrar usuario: " + e.getMessage());
        }
        return false;
    }

    // 🔹 LISTAR TODOS LOS USUARIOS (para usuarios/lista.jsp)
    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT u.*, r.nombre AS rol_nombre " +
                     "FROM usuarios u " +
                     "JOIN roles r ON u.rol_id = r.id " +
                     "ORDER BY u.id";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapUsuario(rs));
            }

        } catch (SQLException e) {
            System.out.println("❌ ERROR SQL al listar usuarios: " + e.getMessage());
        }

        return lista;
    }

    // 🔹 BUSCAR USUARIO POR ID (para editar)
    public Usuario buscarPorId(int id) {
        String sql = "SELECT u.*, r.nombre AS rol_nombre " +
                     "FROM usuarios u " +
                     "JOIN roles r ON u.rol_id = r.id " +
                     "WHERE u.id = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUsuario(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ ERROR SQL al buscar usuario por id: " + e.getMessage());
        }

        return null;
    }

    // 🔹 ACTUALIZAR USUARIO (desde formulario de edición)
    public boolean actualizar(Usuario u) {
        String sql = "UPDATE usuarios " +
                     "SET nombre = ?, correo = ?, usuario = ?, contrasena = ?, rol_id = ?, activo = ? " +
                     "WHERE id = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getCorreo());
            ps.setString(3, u.getUsuario());
            ps.setString(4, u.getContrasena());
            ps.setInt(5, u.getRol().getId());
            ps.setBoolean(6, u.isActivo());
            ps.setInt(7, u.getId());

            int filas = ps.executeUpdate();
            System.out.println("Filas actualizadas: " + filas);
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("❌ ERROR SQL al actualizar usuario: " + e.getMessage());
        }

        return false;
    }

    // 🔹 DESACTIVAR USUARIO (poner activo = 0)
    public boolean desactivar(int id) {
        String sql = "UPDATE usuarios SET activo = 0 WHERE id = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            System.out.println("Usuarios desactivados: " + filas);
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("❌ ERROR SQL al desactivar usuario: " + e.getMessage());
        }

        return false;
    }
}
