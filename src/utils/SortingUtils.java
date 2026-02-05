package utils;

import model.Activity;
import model.MoodEntry;

import java.util.List;

public class SortingUtils {

    public static List<Activity> sortByDateDesc(List<Activity> list) {
        return list.stream()
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .toList();
    }

    public static List<MoodEntry> filterMoodAbove(List<Activity> list, int threshold) {
        return list.stream()
                .filter(a -> a instanceof MoodEntry m && m.getMoodLevel() > threshold)
                .map(a -> (MoodEntry) a)
                .toList();
    }
}
