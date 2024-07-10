# Тестовое задание
Консольное приложение, реализующее функционал формирования чека в магазине
## Содержание 
- [Технологии](#технологии)
- [Инструкция по запуску](#инструкция-по-запуску)
## Технологии
- Java 21
- Gradle 8.7
- JUnit5
- Mockito
## Инструкция по запуску
1. Скачайте или склонируйте репозиторий.
2. Создайте базу данных PostgreSQL c username - postgres и password - postgres (или любыми другими, но тогда не забудьте их поменять в 4 задании), DDL/DML операции которой хранятся в src/main/resources/data.sql.
3. Соберите проект в jar файл c помощью команды:
```
./gradlew jar
```
3. Перейдите в директорию с jar файлом:
```
cd build/libs
```
4. Запустите проект с помощью команды:
```
java -jar TestWithDb-1.0-SNAPSHOT.jar 3-1 2-5 5-1 discountCard=1111 balanceDebitCard=100 saveToFile=./result.csv datasource.url=jdbc:postgresql://localhost:5432/check datasource.username=postgres datasource.password=postgres
```
5. Чек будет сгенерирован и сохранен в файле result.csv в корне проекта, а также выведен в консоль.
