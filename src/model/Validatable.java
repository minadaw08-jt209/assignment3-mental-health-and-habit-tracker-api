package model;

import exception.InvalidInputException;

public interface Validatable {
    void validate();

    default void requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new InvalidInputException(message);
        }
    }

    static void requirePositive(int value, String message) {
        if (value <= 0) {
            throw new InvalidInputException(message);
        }
    }
}



