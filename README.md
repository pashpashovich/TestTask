# Тестовое задание
Консольное приложение, реализующее функционал формирования чека в магазине
## Содержание 
- [Технологии](#технологии)
- [Инструкция по запуску](#инструкция-по-запуску)
## Технологии
- Java 21
## Инструкция по запуску
1. Скачайте или склонируйте репозиторий.
2. Соберите проект.
3. Запустите проект с помощью команды:
```
java -cp "Полный путь к папке с проектом" main.java.ru.clevertec.check.CheckRunner id-quantity discountCard=xxxx balanceDebitCard=xxxx
```
Например,
```
java -cp "C:\Users\Павел\OneDrive\Рабочий стол\2 курс\4 семестр\Java\check\out\production\check" main.java.ru.clevertec.check.CheckRunner 3-1 2-5 5-1 discountCard=1111 balanceDebitCard=100
```
Или с помощью такой команды:
```
java -cp src ./src/main/java/ru/clevertec/check/CheckRunner.java 3-1 2-5 5-1 discountCard=1111 balanceDebitCard=100
```