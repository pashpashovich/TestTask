package main.java.ru.clevertec.check.exceptions;

public class IncorrectRequestException extends AbstractException{

    public IncorrectRequestException(String message,String path) {
        super(message);
        writeErrorToCSV(getExceptionStatus().name(),path);
    }

    @Override
    public String toString() {
        return "IncorrectRequestException: " + getMessage()+" Статус: "+getExceptionStatus();
    }
    @Override
    public ExceptionStatus getExceptionStatus() {
        return ExceptionStatus.BAD_REQUEST;
    }
}
