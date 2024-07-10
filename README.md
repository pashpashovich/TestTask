# Задание
Backend Restful API формирования чека в магазине
## Содержание 
- [Технологии](#технологии)
- [Инструкция по запуску](#инструкция-по-запуску)
- [API](#API)
## Технологии
- Java 21
- Gradle 8.7
- Servlets Jakarta
## Инструкция по запуску
1. Скачайте или склонируйте репозиторий.
2. Добавьте в конец файла catalina.properties, находящегося в папке с вашим Tomcat (пример: ...\apache-tomcat-10.1.17\conf\catalina.properties), следующие системные переменные:
```
datasource.url=jdbc:postgresql://localhost:5432/check
datasource.username=postgres
datasource.password=postgres 
```
3. Соберите проект в war файл c помощью команды:
```
./gradlew clean build
```
4. Подключите Tomcat 10 и добавьте артефакт, который был сгенерирован в build/libs с расширением .war:
```
build/libs/clevertec-check-1.0-SNAPSHOT.war
```
5. Запустите сервер Tomcat.
6. Можете выполнять запросы, которые определены в разделе API,с помощью Postman или другого инструмента тестирования API.
## API
- POST http://localhost:8080/check

Пример тела запроса:
```
{
"products": [
{
"id": 1,
"quantity": 5
},
{
"id": 1,
"quantity": 5
}
],
"discountCard": 1234,
"balanceDebitCard": 100
}
```
Пример ответа:
```
Date;Time
10.07.2024;20:07:31

QTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL
10;Milk;1.07$;1.07$;10.70$

TOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT
10.70$;1.07$;9.63$
```
- GET http://localhost:8080/products?id=1

Пример ответа:
```
{"price":1.07,"id":1,"name":"Milk","quantityInStock":10,"wholesale":true}
```
- POST http://localhost:8080/products

Пример тела запроса:
```
{
"description": "Eat 100g.",
"price": 3.25,
"quantity": 5,
"isWholesale": true
}
```
Пример ответа:
```
{
    "сообщение": "Продукт добавлен успешно"
}
```
- PUT http://localhost:8080/products?id=22

Пример тела запроса:
```
{
"description": "Chocolate Ritter sport 100g.",
"price": 3.25,
"quantity": 5,
"isWholesale": true
}
```
Пример ответа:
```
{
    "сообщение": "Продукт обновлен успешно"
}
```
- DELETE http://localhost:8080/products?id=1

Пример ответа:
```
{"сообщение":"Продукт удален успешно"}
```
- GET http://localhost:8080/discountcards?id=1

Пример ответа:
```
{
    "discountCard": 5265,
    "discountAmount": 2
}
```
- POST http://localhost:8080/discountcards

Пример тела запроса:
```
{
"discountCard": 5265,
"discountAmount": 2
}
```
Пример ответа:
```
{
    "сообщение": "Дисконтная карта добавлена успешно"
}
```
- PUT http://localhost:8080/discountcards?id=10

Пример тела запроса:
```
{
"discountCard": 6786,
"discountAmount": 3
}
```
Пример ответа:
```
{
    "сообщение": "Дисконтная карта обновлена успешно"
}
```
- DELETE http://localhost:8080/discountcards?id=10

Пример ответа:
```
{"сообщение":"Дисконтная карта удалена успешно"}
```
