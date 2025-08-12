...Коротко
- Проект: Java 17, Maven, Hibernate + PostgreSQL.
- В этой ветке основная точка входа для GPT‑варианта: gpt5.MainGPT.
- В проекте есть также jm.task.core.jdbc.Main.

Требования
- Java 17
- Maven 3.x
- PostgreSQL (локально или в Docker)
- (опционально) Docker / GitHub Codespaces

Настройка базы данных (Postgres)

Вариант A — локальный Postgres (Homebrew):
1. Убедиться, что PostgreSQL запущен:
   brew services start postgresql@14
2. Подключиться и создать пользователя/базу (или выполнить команды от суперпользователя):
   psql -h localhost -p 5432 -U postgres
   В psql:
   CREATE ROLE "user" WITH LOGIN PASSWORD 'pass';
   CREATE DATABASE db OWNER "user";
   \q

Вариант B — быстрый запуск в Docker:
docker run --name pp_pg -e POSTGRES_USER=user -e POSTGRES_PASSWORD=pass -e POSTGRES_DB=db -p 5432:5432 -d postgres:15

Конфигурация Hibernate
- Откройте/создайте файл src/main/resources/hibernate.cfg.xml и укажите подключение к PostgreSQL:

<?xml version='1.0' encoding='utf-8'?>
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
  <session-factory>
    <property name="hibernate.connection.driver_class">org.postgresql.Driver</property>
    <property name="hibernate.connection.url">jdbc:postgresql://localhost:5432/db</property>
    <property name="hibernate.connection.username">user</property>
    <property name="hibernate.connection.password">pass</property>
    <property name="hibernate.dialect">org.hibernate.dialect.PostgreSQLDialect</property>
    <property name="hibernate.hbm2ddl.auto">update</property>
    <property name="show_sql">true</property>
  </session-factory>
</hibernate-configuration>

Сборка и запуск локально

1) Собрать проект (подтянутся зависимости):
mvn -B -DskipTests package

2) Запуск (варианты):

a) Через Maven exec (если хотите указать main на лету):
mvn exec:java -Dexec.mainClass="gpt5.MainGPT" -Dexec.classpathScope=runtime

b) Если не используется exec-plugin:
mvn dependency:copy-dependencies -DincludeScope=runtime
java -cp target/classes:target/dependency/* gpt5.MainGPT

(На macOS/Linux classpath разделяется двоеточием ':'; на Windows — ';'.)

Если нужно запустить альтернативный main:
mvn exec:java -Dexec.mainClass="jm.task.core.jdbc.Main"

Добавление exec-maven-plugin (опционально)
Чтобы запускать просто mvn exec:java, можно добавить в pom.xml в секцию <build><plugins>:

<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>exec-maven-plugin</artifactId>
  <version>3.1.0</version>
  <configuration>
    <mainClass>gpt5.MainGPT</mainClass>
  </configuration>
</plugin>

Запуск в GitHub Codespaces (коротко)
- Положите в ветку файлы .devcontainer/devcontainer.json и .devcontainer/docker-compose.yml (если хотите автоматическую сборку и поднятие Postgres).
- Открыть репозиторий → Code → Open with Codespaces → New codespace.
- В терминале Codespaces выполнить:
  mvn -B -DskipTests package
  mvn exec:java -Dexec.mainClass="gpt5.MainGPT"

Полезные команды для диагностики
- Проверить, слушает ли порт 5432:
  lsof -iTCP:5432 -sTCP:LISTEN
- Проверить готовность Postgres:
  pg_isready -h localhost -p 5432
- Логи контейнера Docker:
  docker logs pp_pg

Частые проблемы
- ClassNotFoundException gpt5.MainGPT — выполните mvn package и проверьте правильность FQN.


- PSQLException: Connection refused — Postgres не запущен или неправильный URL/порт/учётные данные в hibernate.cfg.xml.
- NoClassDefFoundError — зависимости не в classpath; используйте mvn exec:java или java -cp target/classes:target/dependency/* ....

Если что не работает — пришлите:
- Вывод mvn -B -DskipTests package (полностью).
- Вывод запуска: mvn exec:java -Dexec.mainClass="gpt5.MainGPT" или java -cp ... gpt5.MainGPT.
- Вывод psql/pg_isready или docker logs pp_pg.

Контакты / помощь
Если нужно — могу подготовить патч, который добавит:
- .devcontainer/ (devcontainer.json + docker-compose.yml)
- exec-plugin в pom.xml
и дать точные команды git для автоматического применения в ветке feature/gpt5 — скажите «да, присылай патч»....
