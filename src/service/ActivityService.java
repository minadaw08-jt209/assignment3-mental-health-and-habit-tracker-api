package service;

import exception.DatabaseOperationException;
import exception.DuplicateResourceException;
import exception.InvalidInputException;
import exception.ResourceNotFoundException;
import model.Activity;
import repository.ActivityRepository;
import repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

public class ActivityService {
    private final ActivityRepository repo = new ActivityRepository();
    private final UserRepository users = new UserRepository();

    public int create(Activity a) {
        a.validate(); // interface Validatable
        if (a.getDate().isAfter(LocalDate.now())) {
            throw new InvalidInputException("Date cannot be in the future");
        }
        if (!users.exists(a.getUserId())) {
            throw new ResourceNotFoundException("User not found: id=" + a.getUserId());
        }

        try {
            return repo.create(a);
        } catch (DatabaseOperationException e) {
            // Postgres unique violation often contains 'duplicate key'
            String msg = e.getCause() == null ? e.getMessage() : e.getCause().getMessage();
            msg = msg == null ? "" : msg.toLowerCase();
            if (msg.contains("duplicate") || msg.contains("uq_user_date_type")) {
                throw new DuplicateResourceException("Duplicate activity for user/date/type (daily summary rule)");
            }
            throw e;
        }
    }

    public List<Activity> getAll() {
        return repo.getAll();
    }

    public Activity getById(int id) {
        if (id <= 0) throw new InvalidInputException("Activity id must be > 0");
        return repo.getById(id).orElseThrow(() ->
                new ResourceNotFoundException("Activity not found: id=" + id));
    }

    public void update(int id, Activity a) {
        a.validate();
        if (!users.exists(a.getUserId())) {
            throw new ResourceNotFoundException("User not found: id=" + a.getUserId());
        }
        if (!repo.update(id, a)) {
            throw new ResourceNotFoundException("Activity not found for update: id=" + id);
        }
    }

    public void delete(int id) {
        if (!repo.delete(id)) {
            throw new ResourceNotFoundException("Activity not found for delete: id=" + id);
        }
    }
}
