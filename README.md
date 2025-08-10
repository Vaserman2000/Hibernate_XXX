Название проекта
Краткое описание
Это учебный проект по JDBC и Hibernate (Практика 3.4). Содержит реализацию DAO на JDBC и на Hibernate, сервисный слой и тесты.

Что внутри
- src/main/java — реализация приложения (JDBC и Hibernate DAO)
- src/test/java — тесты
- pom.xml — сборка Maven

Требования
- JDK 11+ (или версия, указанная в pom.xml)
- Maven 3.6+
- Локально: MySQL/Postgres или H2 (рекомендую использовать H2 для тестов)

Быстрый старт (локально)
1) Сборка и запуск тестов:
mvn clean test

2) Запуск приложения (если есть main):
mvn -DskipTests=false package
java -jar target/your-artifact.jar
(замени your-artifact.jar на фактическое имя в target)

Конфигурация базы данных
- Для разработки/продакшн настройте src/main/resources/hibernate.cfg.xml (или application.properties) с параметрами вашей БД (URL, пользователь, пароль, dialect).
- Для тестов рекомендуется использовать in-memory H2; пример тестовой конфигурации приведён ниже.

Настройка CI (GitHub Actions)
Добавлен workflow .github/workflows/maven.yml, который собирает проект и запускает тесты при push/PR к main.

Советы по Hibernate и маппингам
- Убедитесь, что entity-классы помечены @Entity и при необходимости @Table(name="...").
- У каждого Entity должн быть первичный ключ с @Id и стратегия генерации, например @GeneratedValue(strategy = GenerationType.IDENTITY).
- Нужен публичный конструктор без параметров.
- Реализуйте геттеры/сеттеры, equals()/hashCode() (по бизнес-ключу) и toString() аккуратно.
- Конфигурация (hibernate.cfg.xml или persistence.xml) должна содерж
