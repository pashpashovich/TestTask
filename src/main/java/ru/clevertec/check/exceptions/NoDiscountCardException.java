package ru.clevertec.check.exceptions;

public class NoDiscountCardException extends Exception {
    public NoDiscountCardException(String message) {
        super(message);
    }
    @Override
    public String toString() {
        return "NoDiscountCardException: " + getMessage();
    }

}
