import controller.ActivityController;
import model.Activity;
import model.Habit;
import model.MoodEntry;
import repository.ActivityRepository;
import repository.UserRepository;
import service.ActivityServiceImpl;
import service.interfaces.IActivityService;
import utils.ReflectionUtils;
import utils.SortingUtils;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        ActivityRepository activityRepo = new ActivityRepository();
        UserRepository userRepo = new UserRepository();

        IActivityService service = new ActivityServiceImpl(activityRepo, userRepo, userRepo);
        ActivityController controller = new ActivityController(service);

        Activity habit = new Habit(0, "Morning Walk", 1, LocalDate.now(), 5);
        Activity mood  = new MoodEntry(0, "Daily Mood",  1, LocalDate.now(), 8);

        controller.create(habit);
        controller.create(mood);

        controller.list();

        controller.create(new MoodEntry(0, "Bad Mood", 1, LocalDate.now(), 99));

        controller.create(new MoodEntry(0, "Daily Mood Again", 1, LocalDate.now(), 7));

        var list = service.getAll();

        System.out.println("Sorted by date:");
        SortingUtils.sortByDateDesc(list).forEach(a -> System.out.println(a.summary()));

        System.out.println("Reflection:");
        if (!list.isEmpty()) {
            ReflectionUtils.printInfo(list.get(0));
        } else {
            System.out.println("(no activities to inspect)");
        }
    }
}
