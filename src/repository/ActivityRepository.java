package repository;

import exception.DatabaseOperationException;
import model.Activity;
import model.Habit;
import model.MoodEntry;
import utils.Db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActivityRepository {

    public int create(Activity a) {
        String sql = """
            INSERT INTO activities(user_id,name,type,entry_date,habit_streak,mood_level)
            VALUES (?,?,?::activity_type,?,?,?) RETURNING id
        """;

        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            bind(ps, a);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Create activity failed", e);
        }
    }

    public List<Activity> getAll() {
        String sql = "SELECT * FROM activities ORDER BY id";
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Activity> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Get all activities failed", e);
        }
    }

    public Optional<Activity> getById(int id) {
        String sql = "SELECT * FROM activities WHERE id=?";
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Get activity by id failed", e);
        }
    }

    public boolean update(int id, Activity a) {
        String sql = """
            UPDATE activities
               SET user_id=?, name=?, type=?::activity_type, entry_date=?, habit_streak=?, mood_level=?
             WHERE id=?
        """;

        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            bind(ps, a);
            ps.setInt(7, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Update activity failed", e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM activities WHERE id=?";
        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Delete activity failed", e);
        }
    }

    private void bind(PreparedStatement ps, Activity a) throws SQLException {
        ps.setInt(1, a.getUserId());
        ps.setString(2, a.getName());
        ps.setString(3, a.toDbType());
        ps.setDate(4, Date.valueOf(a.getDate()));

        if (a instanceof Habit h) {
            ps.setInt(5, h.getStreak());
            ps.setNull(6, Types.INTEGER);
        } else { // MoodEntry
            MoodEntry m = (MoodEntry) a;
            ps.setNull(5, Types.INTEGER);
            ps.setInt(6, m.getMoodLevel());
        }
    }

    private Activity map(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int userId = rs.getInt("user_id");
        String name = rs.getString("name");
        String type = rs.getString("type");
        LocalDate date = rs.getDate("entry_date").toLocalDate();

        if ("HABIT".equals(type)) {
            return new Habit(id, name, userId, date, rs.getInt("habit_streak"));
        }
        return new MoodEntry(id, name, userId, date, rs.getInt("mood_level"));
    }
}
