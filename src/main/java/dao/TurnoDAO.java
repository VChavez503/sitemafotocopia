package dao;

import model.Turno;
import util.Conexion;

import java.sql.*;

public class TurnoDAO {

    public Turno obtenerTurnoActual() {
        Turno t = null;
        String sql = "SELECT * FROM turnos WHERE CURTIME() BETWEEN hora_inicio AND hora_fin LIMIT 1";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                t = new Turno();
                t.setId(rs.getInt("id"));
                t.setNombre(rs.getString("nombre"));
                t.setHoraInicio(rs.getTime("hora_inicio"));
                t.setHoraFin(rs.getTime("hora_fin"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
