package model;

import java.time.LocalDate;

public abstract class Activity extends BaseEntity {
    protected int userId;
    protected LocalDate date;

    protected Activity(int id, String name, int userId, LocalDate date) {
        super(id, name);
        this.userId = userId;
        this.date = date;
    }

    public int getUserId() { return userId; }
    public LocalDate getDate() { return date; }
}




