package repository;

import exception.DatabaseOperationException;
import utils.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    public boolean exists(int id) {
        String sql = "SELECT 1 FROM users WHERE id=?";
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("User exists check failed", e);
        }
    }
}

