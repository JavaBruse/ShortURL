package org.MIFI;

import org.MIFI.service.LinkService;
import org.MIFI.service.UserService;

import java.util.Arrays;
import java.util.Scanner;


public class CoreApp {
    private LinkService linkService;
    private UserService userService;
    private String UUID = null;
    private Scanner scanner;

    public CoreApp() {
        this.linkService = new LinkService();
        this.userService = new UserService();
        this.scanner = new Scanner(System.in);
    }

    public void start() {

        System.out.println("Добро пожаловавть в Сервис коротких ссылок!\n Для просмотра команд введите help");
        authUUID();


        while (true) {
            String input = scanner.nextLine();
            switch ("") {
                case "-help":
                    help();
                    break;
                case "-l":

                    break;
                case "-e":
                    return;
                default:
                    System.out.println("Ошибка ввода, обратитесь к справке -help");
            }
        }
    }

    private String newShort(String[] longLink) {
        //linkService.addNewLink()
        return "";
    }

    private void help() {
        System.out.println("Справка");
        if (UUID != null) {
            System.out.println(
                    "-l [URL] - создание короткой ссылки \n" +
                            "-e      - выход");
        } else {
            System.out.println(
                    "-n [имя пользователя] -  создание нового пользователя\n" +
                            "-u [UUID]             -  вход по UUID");
        }

    }

    private void authUUID() {
        System.out.println("Введите имя нового пользователя, или UUID существующего:");
        while (true) {
            String nameOrUUID = scanner.nextLine();
            String[] line = nameOrUUID.split(" ");

            if (line.length <= 1 && !line[0].equals("-help")) {
                System.out.println("Ошибка ввода, обратитесь к справке -help");
                continue;
            }
            switch (line[0]) {
                case "-n":
                    this.UUID = userService.addUser(line[1]).getUUID();
                    System.out.println("Поздравляю с регистрацией " + line[1] + ", сохраниете UUID для входа.\nUUID: " + UUID);
                    return;
                case "-u":
                    if (userService.checkUserUUID(nameOrUUID)) {
                        this.UUID = nameOrUUID;
                        System.out.println("С возвращением " + userService.getByUUID(this.UUID).getName() + "!");
                        return;
                    }
                    break;
                case "-help": {
                    help();
                    break;
                }
                default:
                    System.out.println("Ошибка ввода, обратитесь к справке -help");
            }
        }
    }
}
