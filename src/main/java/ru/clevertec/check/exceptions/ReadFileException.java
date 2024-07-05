package ru.clevertec.check.exceptions;

public class ReadFileException extends AbstractException {

    public ReadFileException(String message,String path) {
        super(message);
        writeErrorToCSV(getExceptionStatus().name(),path);
    }

    @Override
    public String toString() {
        return "ReadFileException: " + getMessage()+" Статус: "+getExceptionStatus();
    }

    @Override
    public ExceptionStatus getExceptionStatus() {
        return ExceptionStatus.BAD_REQUEST;
    }
}
