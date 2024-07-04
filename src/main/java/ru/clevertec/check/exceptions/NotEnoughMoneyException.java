package main.java.ru.clevertec.check.exceptions;

public class NotEnoughMoneyException extends AbstractException {
    public NotEnoughMoneyException(String message) {
        super(message);
        writeErrorToCSV(getExceptionStatus().name());
    }

    @Override
    public String toString() {
        return "NotEnoughMoneyException: " + getMessage()+" Статус: "+getExceptionStatus();
    }

    @Override
    public ExceptionStatus getExceptionStatus() {
        return ExceptionStatus.NOT_ENOUGH_MONEY;
    }

}
