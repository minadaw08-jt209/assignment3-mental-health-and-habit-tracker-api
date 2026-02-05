package model;

public class User {
    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        setName(name);
    }

    public int getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("User name must not be empty");
        }
        this.name = name;
    }
}

