package model;

import exception.InvalidInputException;

import java.time.LocalDate;

public class Habit extends Activity {
    private final int streak;

    public Habit(int id, String name, int userId, LocalDate date, int streak) {
        super(id, name, userId, date);
        this.streak = streak;
    }

    public int getStreak() { return streak; }

    @Override public String getType() { return "Habit"; }
    @Override public String toDbType() { return "HABIT"; }

    @Override public double calculateScore() {
        return streak * 1.5;
    }

    @Override public void validate() {
        if (name == null || name.isBlank()) throw new InvalidInputException("Habit name must not be empty");
        if (userId <= 0) throw new InvalidInputException("Habit userId must be > 0");
        if (date == null) throw new InvalidInputException("Habit date is required");
        if (streak < 0) throw new InvalidInputException("Habit streak must be >= 0");
    }
}
