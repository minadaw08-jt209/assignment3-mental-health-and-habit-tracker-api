package model;

import exception.InvalidInputException;

import java.time.LocalDate;

public class MoodEntry extends Activity {
    private final int moodLevel; // 1..10

    public MoodEntry(int id, String name, int userId, LocalDate date, int moodLevel) {
        super(id, name, userId, date);
        this.moodLevel = moodLevel;
    }

    public int getMoodLevel() { return moodLevel; }

    @Override public String getType() { return "MoodEntry"; }
    @Override public String toDbType() { return "MOOD"; }

    @Override public double calculateScore() {
        return moodLevel;
    }

    @Override public void validate() {
        if (name == null || name.isBlank()) throw new InvalidInputException("Mood entry name must not be empty");
        if (userId <= 0) throw new InvalidInputException("Mood userId must be > 0");
        if (date == null) throw new InvalidInputException("Mood date is required");
        if (moodLevel < 1 || moodLevel > 10) throw new InvalidInputException("Mood level must be in range 1..10");
    }
}



