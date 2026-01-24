package model;

public abstract class BaseEntity implements Validatable, Trackable {
    protected int id;
    protected String name;

    protected BaseEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // abstract method #1
    public abstract String getType();

    // abstract method #2 (для БД: HABIT/MOOD)
    public abstract String toDbType();

    // concrete method (готовая реализация)
    public String summary() {
        return getType() + "{id=" + id + ", name='" + name + "'}";
    }

    public int getId() { return id; }
    public String getName() { return name; }
}


