package main.java.ru.clevertec.check.exceptions;

import main.java.ru.clevertec.check.models.Product;

public class ExceptionFactory {

    public static Exception createException(ExceptionType type, int productId, Product product, int quantity, String discountCardNumber) {
        switch (type) {
            case READFILEEXCEPTION -> {
                return new ReadFileException("Ошибка...");
            }
            case NOPRODUCTEXCEPTION -> {
                return new NoProductException("Товар с ID " + productId + " не найден");
            }
            case INSUFFICIENTSTOCKEXCEPTION -> {
                return new InsufficientStockException("Недостаточно товара " + product.getName() + " на складе. Доступно: " + product.getQuantityInStock() + ". Запрошено: " + quantity);
            }
            case NODISCOUNTCARDEXCEPTION -> {
                return new NoDiscountCardException("Скидочной карты с номером " + discountCardNumber + " не существует");
            }
            case INCORRECTREQUESTEXCEPTION -> {
                return new IncorrectRequestException("Неверный формат запроса(");
            }
            case NOTENOUGHMONEYEXCEPTION -> {
                return new NotEnoughMoneyException("Недостаточно средств для оплаты...");
            }
        }
        return null;
    }
}
