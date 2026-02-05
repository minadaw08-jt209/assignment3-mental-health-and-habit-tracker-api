package service.interfaces;

import model.Activity;
import java.util.List;

public interface IActivityService {
    int create(Activity a);
    List<Activity> getAll();
    Activity getById(int id);
    void update(int id, Activity a);
    void delete(int id);
}


