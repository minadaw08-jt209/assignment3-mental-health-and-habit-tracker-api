package controller;

import exception.DatabaseOperationException;
import exception.DuplicateResourceException;
import exception.InvalidInputException;
import exception.ResourceNotFoundException;
import model.Activity;
import service.ActivityService;

public class ActivityController {
    private final ActivityService service = new ActivityService();

    private static String ok(String m) {
        return "{\"status\":\"OK\",\"message\":\"" + esc(m) + "\"}";
    }
    private static String err(String type, String m) {
        return "{\"status\":\"ERROR\",\"errorType\":\"" + esc(type) + "\",\"message\":\"" + esc(m) + "\"}";
    }
    private static String esc(String s) {
        return s == null ? "" : s.replace("\"", "\\\"");
    }

    public void create(Activity a) {
        try {
            int id = service.create(a);
            System.out.println(ok("created activity id=" + id));
        } catch (InvalidInputException | ResourceNotFoundException e) {
            System.out.println(err(e.getClass().getSimpleName(), e.getMessage()));
        } catch (DatabaseOperationException e) {
            System.out.println(err(
                    "DatabaseOperationException",
                    e.getMessage() + " | cause=" + (e.getCause() == null ? "null" : e.getCause().getMessage())
            ));
        }
    }

    public void list() {
        try {
            var list = service.getAll();
            System.out.println(ok("count=" + list.size()));
            for (Activity a : list) {
                // polymorphism + interface Trackable
                System.out.println(" - " + a.summary() + ", score=" + a.calculateScore());
            }
        } catch (RuntimeException e) {
            System.out.println(err(e.getClass().getSimpleName(), e.getMessage()));
        }
    }
}

