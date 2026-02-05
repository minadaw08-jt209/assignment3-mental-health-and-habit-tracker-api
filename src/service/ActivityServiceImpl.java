package service;

import exception.DatabaseOperationException;
import exception.DuplicateResourceException;
import exception.InvalidInputException;
import exception.ResourceNotFoundException;
import model.Activity;
import model.User;
import repository.UserRepository;
import repository.interfaces.CrudRepository;
import service.interfaces.IActivityService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ActivityServiceImpl implements IActivityService {

    private final CrudRepository<Activity> activityRepo;
    private final CrudRepository<User> userRepo;
    private final UserRepository users;

    public ActivityServiceImpl(CrudRepository<Activity> activityRepo,
                               CrudRepository<User> userRepo,
                               UserRepository users) {
        this.activityRepo = activityRepo;
        this.userRepo = userRepo;
        this.users = users;
    }

    @Override
    public int create(Activity a) {
        if (a == null) throw new InvalidInputException("Activity is null");

        a.validate();

        if (a.getDate().isAfter(LocalDate.now())) {
            throw new InvalidInputException("Date cannot be in the future");
        }

        if (!users.exists(a.getUserId())) {
            throw new ResourceNotFoundException("User not found: id=" + a.getUserId());
        }

        try {
            return activityRepo.create(a);

        } catch (DatabaseOperationException e) {
            Throwable cause = e.getCause();
            if (cause instanceof SQLException se && "23505".equals(se.getSQLState())) {
                throw new DuplicateResourceException("Duplicate activity for user/date/type (daily summary rule)");
            }

            String msg = (cause == null ? e.getMessage() : cause.getMessage());
            msg = msg == null ? "" : msg.toLowerCase();
            if (msg.contains("duplicate") || msg.contains("uq_user_date_type")) {
                throw new DuplicateResourceException("Duplicate activity for user/date/type (daily summary rule)");
            }

            throw e;
        }
    }

    @Override
    public List<Activity> getAll() {
        return activityRepo.getAll();
    }

    @Override
    public Activity getById(int id) {
        if (id <= 0) throw new InvalidInputException("Activity id must be > 0");

        return activityRepo.getById(id).orElseThrow(() ->
                new ResourceNotFoundException("Activity not found: id=" + id));
    }

    @Override
    public void update(int id, Activity a) {
        if (id <= 0) throw new InvalidInputException("Activity id must be > 0");
        if (a == null) throw new InvalidInputException("Activity is null");

        a.validate();

        if (!users.exists(a.getUserId())) {
            throw new ResourceNotFoundException("User not found: id=" + a.getUserId());
        }

        if (!activityRepo.update(id, a)) {
            throw new ResourceNotFoundException("Activity not found for update: id=" + id);
        }
    }

    @Override
    public void delete(int id) {
        if (id <= 0) throw new InvalidInputException("Activity id must be > 0");

        if (!activityRepo.delete(id)) {
            throw new ResourceNotFoundException("Activity not found for delete: id=" + id);
        }
    }
}
