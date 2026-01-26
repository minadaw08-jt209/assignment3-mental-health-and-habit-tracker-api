package model;

public abstract class BaseEntity implements Validatable, Trackable {
    protected int id;
    protected String name;

    protected BaseEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public abstract String getType();

    public abstract String toDbType();

    public String summary() {
        return getType() + "{id=" + id + ", name='" + name + "'}";
    }

    public int getId() { return id; }
    public String getName() { return name; }
}


