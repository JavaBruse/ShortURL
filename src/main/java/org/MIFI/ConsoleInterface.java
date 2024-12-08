package org.MIFI;

import org.MIFI.entity.Link;
import org.MIFI.exceptions.NotFoundEntityException;
import org.MIFI.exceptions.TimeErrorException;
import org.MIFI.exceptions.URLNotCorrect;
import org.MIFI.service.LinkService;
import org.MIFI.service.UserService;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;


public class ConsoleInterface {
    private LinkService linkService;
    private UserService userService;
    private String UUID = null;
    private Scanner scanner;

    public ConsoleInterface() {
        this.linkService = new LinkService();
        this.userService = new UserService();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Добро пожаловавть в Сервис коротких ссылок!\nДля просмотра команд введите help");
        authUUID();
        while (true) {
            String input = scanner.nextLine();
            String[] line = input.split(" ");
            if (linkService.isShort(line[0])) {
                openShortURL(line[0]);
                continue;
            }
            if (line.length <= 1) {
                if (!line[0].equals("help") && !line[0].equals("exit") && !line[0].equals("all")) line[0] = "NotFound";
            }
            switch (line[0]) {
                case "help":
                    help();
                    break;
                case "-l":
                    addNewLink(line);
                    break;
                case "all":
                    getAllLink();
                    break;
                case "exit":
                    return;
                default:
                    System.err.println("Ошибка ввода, обратитесь к справке help");
            }
        }
    }

    private void addNewLink(String[] line) {
        String link;
        try {
            if (line.length == 2) {
                link = linkService.addNewLink(this.UUID, line[1]).getShortLink();
            } else {
                try {
                    link = linkService.addNewLink(this.UUID, line[1], line[2]).getShortLink();
                } catch (TimeErrorException e) {
                    System.err.println(e.getMessage());
                    return;
                }
            }
        } catch (URLNotCorrect e) {
            System.err.println(e.getMessage());
            return;
        }
        System.out.println("Ваша коротка ссылка готова: " + link);
    }

    private void getAllLink() {
        try {
            ArrayList<Link> links = linkService.findByUUID(this.UUID);
            for (Link l : links) {
                System.out.println(l);
            }
        } catch (NotFoundEntityException e) {
            System.err.println(e.getMessage());
        }
    }

    private void help() {
        System.out.println("Справка");
        if (UUID != null) {
            System.out.println(
                    "-l [URL]                  - создание короткой ссылки, без параметров.\n" +
                            "-l [URL] -h [часы:минуты] - параметр существования ссылки, например: -h 5:10 где 5 часов, 10 минут.\n" +
                            "exit                      - выход\n" +
                            "all                       - все мои ссылки.");
        } else {
            System.out.println(
                    "[shortUrl]            -  откроет коротку ссылку, если она валидна\n" +
                            "-n [имя пользователя] -  создание нового пользователя\n" +
                            "-u [UUID]             -  вход по UUID\n" +
                            "exit                  -  выход");
        }
    }

    private void authUUID() {
        System.out.println("Введите имя нового пользователя, или UUID существующего:");
        while (true) {
            String nameOrUUID = scanner.nextLine();
            String[] line = nameOrUUID.split(" ");
            if (linkService.isShort(line[0])) {
                openShortURL(line[0]);
                continue;
            }
            if (line.length <= 1 && !line[0].equals("help")) {
                System.err.println("Ошибка ввода, обратитесь к справке help");
                continue;
            }
            switch (line[0]) {
                case "-n":
                    this.UUID = userService.addUser(line[1]).getUUID();
                    System.out.println("Поздравляю с регистрацией " + line[1] + ", сохраниете UUID для входа.\nUUID: " + UUID);
                    return;
                case "-u":
                    String name = userService.getByUUID(line[1]);
                    if (name != null) {
                        this.UUID = line[1];
                        System.out.println("С возвращением " + name + "!");
                        return;
                    } else {
                        System.out.println("Неверный UUID, такого в базе нет, откуда он у тебя?");
                    }
                    break;
                case "help":
                    help();
                    break;
                case "exit":
                    System.exit(0);
                default:
                    System.err.println("Ошибка ввода, обратитесь к справке help");
            }
        }
    }

    private boolean openShortURL(String shortURL) {
        try {
            Link link = linkService.getLongLink(shortURL);
            if (linkService != null) {
                try {
                    link.setTransitionLimit(link.getTransitionLimit() - 1);
                    linkService.updateLink(link);
                    Desktop.getDesktop().browse(new URI(link.getLongLink()));
                    System.out.println("Вот Ваша ссылка: " + link.getLongLink());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
                return true;
            }

        } catch (NotFoundEntityException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }
}
