package jm.task.core.jdbc.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import javax.persistence.*;

@Entity // Делает класс сущностью Hibernate
@Table(name = "users", uniqueConstraints =
@UniqueConstraint(columnNames = {"age", "lastname", "name"})) // Указывает на уникальные ограничения
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Можно использовать разные стратегии
    private Long id;

    @Column
    private String name;

    @Column
    @NotNull
    @Size(min = 1, max = 20) // Пример ограничения размера имени
    private String lastName;

    @Column
    @NotNull
    @Size(min = 1, max = 100) // Пример ограничения размера имени
    private Byte age;

    // Конструктор без параметров (необходим для Hibernate)
    public User() {
    }

    public User(String name, String lastName, Byte age) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Byte getAge() {
        return age;
    }
    public void setAge(Byte age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                '}';
    }
}
