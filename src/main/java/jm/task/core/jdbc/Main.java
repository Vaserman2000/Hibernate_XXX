package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        Scanner scanner = new Scanner(System.in);

        try {
            userService.createUsersTable(); // Создаем таблицу пользователей

            // Запрос на ввод данных пользователя
            for (int i = 0; i < 1; i++) { // Позволяем ввести 4 пользователя

                boolean userAdded = false;

                while (!userAdded) {
                    System.out.println("Введите имя пользователяя :");
                    String firstName = scanner.nextLine();

                    System.out.println("Введите фамилию пользователя:");
                    String lastName = scanner.nextLine();

                    System.out.println("Введите возраст пользователя:");
                    byte age;
                    try {
                        age = Byte.parseByte(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Пожалуйста, введите корректный возраст.");
                        continue; // Запрашиваем ввод заново
                    }

                    // Сохраняем пользователя и проверяем, добавлен ли он успешно
                    try {
                        userService.saveUser(firstName, lastName, age);
                        userAdded = true; // Успешно добавлено
                    } catch (Exception e) {
                        System.out.println("Ошибка при добавлении пользователя: " + e.getMessage());
                    }
                }
            }

            // Извлекаем и выводим всех пользователей
            List<User> users = userService.getAllUsers();

            for (User user : users) {
                System.out.println(user); // Вывод информации о пользователе
            }

            // Удаление пользователей по ID
            System.out.print("Введите ID пользователя, которого хотите удалить: ");
            long id = scanner.nextLong(); // Считываем ID
            userService.removeUserById(id); // Удаляем пользователя с указанным ID

            //userService.cleanUsersTable(); // Опциональная чистка, если нужно удалить данные из таблицы

        } catch (Exception e) {
            e.printStackTrace(); // Обработка ошибок для диагностики
        }
        scanner.close(); // Закрываем сканер
    }
}