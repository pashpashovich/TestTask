package ru.clevertec.check.exceptions;


import ru.clevertec.check.models.Product;

public class ExceptionFactory {

    public static Exception createException(ExceptionType type, int productId, Product product, int quantity, String discountCardNumber, String path) {
        switch (type) {
            case READFILEEXCEPTION -> {
                return new ReadFileException("Ошибка...",path);
            }
            case NOPRODUCTEXCEPTION -> {
                return new NoProductException("Товар с ID " + productId + " не найден",path);
            }
            case INSUFFICIENTSTOCKEXCEPTION -> {
                return new InsufficientStockException("Недостаточно товара " + product.getName() + " на складе. Доступно: " + product.getQuantityInStock() + ". Запрошено: " + quantity,path);
            }
            case NODISCOUNTCARDEXCEPTION -> {
                return new NoDiscountCardException("Скидочной карты с номером " + discountCardNumber + " не существует");
            }
            case INCORRECTREQUESTEXCEPTION -> {
                return new IncorrectRequestException("Неверный формат запроса(",path);
            }
            case NOTENOUGHMONEYEXCEPTION -> {
                return new NotEnoughMoneyException("Недостаточно средств для оплаты...",path);
            }
        }
        return null;
    }
}
