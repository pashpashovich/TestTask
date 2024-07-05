package ru.clevertec.check.exceptions;

public class NoProductException extends AbstractException {
    public NoProductException(String message,String path) {
        super(message);
        writeErrorToCSV(getExceptionStatus().name(),path);
    }

    @Override
    public ExceptionStatus getExceptionStatus() {
        return ExceptionStatus.BAD_REQUEST;
    }

    @Override
    public String toString() {
        return "NoProductException: " + getMessage()+" Статус: "+getExceptionStatus();
    }
}
