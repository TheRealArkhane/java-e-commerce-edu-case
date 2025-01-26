# E-commerce marketplace Java Project

## Description

This project represents an API implementation of a marketplace platform for selling a wide range of products.

### General Structure Overview

Products are located in final subcategories, and categories form a hierarchical tree. Each product can have multiple offers, and each offer can have multiple attributes. Offers can be added to the cart, with the total quantity and price calculated. Once the cart is filled, an order can be made by choosing the delivery method (courier or pickup from a pickup point) and payment method (card or cash). For courier delivery, only card payment is available in the app. To validate addresses for courier delivery, integration with the DaData service is implemented. After completing the order, users can view their order history with details for each order.

Admin logic is also implemented. Admins can manage user accounts and data, create/edit/delete categories, products, offers, and attributes. Admins can also add/edit/clear other users' carts and view their order history. Regular users can only manage their profile, cart, place orders, and view their order history. All other entities are read-only. Registration and authentication are implemented via login (email)/password.

- Docker-based containerization for project interaction, with configuration in Docker-compose. Data in the database (users, catalog, related entities) is populated automatically when the container is started;
- Logging is implemented using @Slf4j;
- Business logic is wrapped in unit tests (JUnit5 and Mockito);
- DaData integration requires entering a token and key obtained from DaData.ru (registration is required to get these in your profile); otherwise, placing an order will be impossible.

Swagger documentation link (when running via localhost:8080): http://localhost:8080/swagger-ui/index.html#/

Technologies used in the project:
- Java 17
- Spring Boot
- Spring Data JPA (Hibernate)
- Spring Web
- PostgreSQL
- Docker
- DaData API (for address validation)
- JUnit5 and Mockito (for tests)

Database Schema Image:

![DB Schema](DBSchema.png)

Thank you for your attention, and I look forward to your feedback!
If this project was helpful for educational purposes, please give it a Star on the repository page.

My contact details for feedback:
- fedyanin.v.v@yandex.ru
- https://t.me/fedyanin_1997

---

# E-commerce marketplace Java проект

## Описание

Данный проект представляет собой API-репрезентацию маркетплейс-платформы для реализации товаров широкого профиля.

### Общее описание структуры

Товары лежат в конечных подкатегориях, категории представляют собой иерархическое древо. У каждого товара может быть несколько предложений (офферов), у каждого оффера может быть несколько атрибутов. Оффер может быть добавлен в корзину с подсчетом общего количества добавленных предложений и суммой. При наполненной корзине есть возможность сделать заказ, выбрав способ доставки (курьер, самовывоз из пункта выдачи) и способ оплаты (картой или наличными. При доставке курьером оплата доступна только картой в приложении). Для валидации адресов при доставке курьером реализована интеграция с сервисом DaData. После выполнения заказа пользователь может посмотреть историю своих заказов с деталями каждого заказа.

Также реализована логика для администратора. Он может управлять аккаунтами и данными других пользователей, создавать/изменять/удалять категории, товары, офферы и атрибуты. Администратор также может добавлять/изменять/очищать корзину других пользователей, а также смотреть историю заказов других пользователей. Обычному пользователю доступно только управление своим профилем, корзиной, возможностью сделать заказ и просмотр истории заказов. Для остальных сущностей - read-only. Регистрация и авторизация реализованы через логин(email)/пароль.

- Для взаимодействия с проектом реализована контейнеризация с помощью Docker и конфигурацией в Docker-compose. Данные в БД (пользователи, каталог, связанные сущности) будут занесены автоматически при поднятии контейнера;
- В проекте реализовано логирование через @Slf4j;
- Сервисная логика обернута unit-тестами (jUnit5 и Mockito);
- Интеграция с DaData потребует внесения своего токена и ключа, получаемого через сервис DaData.ru (необходимо зарегистрироваться, чтобы получить их в профиле), в противном случае сделать заказ будет невозможно.

Ссылка на документацию Swagger (при запуске через localhost:8080): http://localhost:8080/swagger-ui/index.html#/

Проект базируется на следующих технологиях:
- Java 17
- Spring Boot
- Spring Data JPA (Hibernate)
- Spring Web
- PostgreSQL
- Docker
- DaData API (для валидации адреса)
- JUnit5 и Mockito (для тестов)

Изображение схемы сущностей в БД:

![Схема БД](DBSchema.png)

Спасибо за внимание, всегда жду обратной связи!
Если проект был полезен вам для образовательных целей, ставьте Star на странице репозитория.

Мои контакты для обратной связи:
- fedyanin.v.v@yandex.ru
- https://t.me/fedyanin_1997
