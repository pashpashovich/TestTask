package main.java.ru.clevertec.check.exceptions;

public class NoDiscountCardException extends AbstractException {
    public NoDiscountCardException(String message) {
        super(message);
        writeErrorToCSV(getExceptionStatus().name());
    }

    @Override
    public String toString() {
        return "NoDiscountCardException: " + getMessage();
    }
    @Override
    public ExceptionStatus getExceptionStatus() {
        return ExceptionStatus.BAD_REQUEST;
    }
}
