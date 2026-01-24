import controller.ActivityController;
import model.Activity;
import model.Habit;
import model.MoodEntry;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        ActivityController c = new ActivityController();

        Activity habit = new Habit(0, "Morning Walk", 1, LocalDate.now(), 5);
        Activity mood  = new MoodEntry(0, "Daily Mood",  1, LocalDate.now(), 8);

        c.create(habit);
        c.create(mood);

        c.list();

        c.create(new MoodEntry(0, "Bad Mood", 1, LocalDate.now(), 99));

        c.create(new MoodEntry(0, "Daily Mood Again", 1, LocalDate.now(), 7));
    }
}
