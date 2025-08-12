package gpt5;

import gpt5.daoGPT.UserHibernateDaoImpl;
import gpt5.daoGPT.UserDaoGPT;
import gpt5.modelGPT.UserGPT;
import gpt5.servisGPT.UserService;
import gpt5.servisGPT.UserServiceImpl;
import gpt5.utilGPT.HibernateUtil;

import java.util.List;
import java.util.Scanner;

public class MainGPT {

    public static void main(String[] args) {
        UserDaoGPT userDaoGPT = new UserHibernateDaoImpl();
        UserService userServiceGPT = new UserServiceImpl(userDaoGPT);

        // Создаём таблицу (если нужно)
        userServiceGPT.createUsersTable();

        try (Scanner scanner = new Scanner(System.in)) {
            boolean exit = false;
            while (!exit) {
                printMenu();
                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1" -> addUserFlow(scanner, userServiceGPT);
                    case "2" -> listUsers(userServiceGPT);
                    case "3" -> removeUserFlow(scanner, userServiceGPT);
                    case "4" -> {
                        userServiceGPT.cleanUsersTable();
                        System.out.println("Таблица очищена.");
                    }
                    case "5" -> {
                        userServiceGPT.dropUsersTable();
                        System.out.println("Таблица удалена.");
                    }
                    case "0" -> exit = true;
                    default -> System.out.println("Неверный выбор. Попробуйте снова.");
                }
            }
        } finally {
            // Закрываем SessionFactory корректно
            HibernateUtil.shutdown();
            System.out.println("Завершение работы.");
        }
    }

    private static void printMenu() {
        System.out.println("\n=== Меню ===");
        System.out.println("1. Добавить пользователя");
        System.out.println("2. Показать всех пользователей");
        System.out.println("3. Удалить пользователя по ID");
        System.out.println("4. Очистить таблицу пользователей");
        System.out.println("5. Удалить таблицу пользователей");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private static void addUserFlow(Scanner scanner, UserService userService) {
        try {
            System.out.print("Имя: ");
            String name = scanner.nextLine();
            System.out.print("Фамилия: ");
            String lastName = scanner.nextLine();
            System.out.print("Возраст: ");
            byte age = Byte.parseByte(scanner.nextLine());
            userService.saveUser(name, lastName, age);
        } catch (NumberFormatException nfe) {
            System.out.println("Возраст должен быть числом (18-100).");
        } catch (IllegalArgumentException iae) {
            System.out.println("Ошибка в данных: " + iae.getMessage());
        } catch (Exception e) {
            System.out.println("Не удалось добавить пользователя: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void listUsers(UserService userService) {
        List<UserGPT> users = userService.getAllUsers();
        if (users == null || users.isEmpty()) {
            System.out.println("Пользователей не найдено.");
        } else {
            users.forEach(System.out::println);
        }
    }

    private static void removeUserFlow(Scanner scanner, UserService userService) {
        try {
            System.out.print("Введите ID пользователя для удаления: ");
            long id = Long.parseLong(scanner.nextLine());
            userService.removeUserById(id);
        } catch (NumberFormatException nfe) {
            System.out.println("ID должен быть числом.");
        } catch (IllegalArgumentException iae) {
            System.out.println("Ошибка: " + iae.getMessage());
        } catch (Exception e) {
            System.out.println("Не удалось удалить пользователя: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

