package org.MIFI;

import org.MIFI.entity.Link;
import org.MIFI.entity.Settings;
import org.MIFI.exceptions.LimitIsOverException;
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
                case "-rm":
                    deleteShortLink(line[1]);
                    break;
                case "-e":
                    editLink(line[1]);
                    break;
                case "exit":
                    System.exit(0);
                default:
                    System.err.println("Ошибка ввода, обратитесь к справке help");
            }
        }
    }

    private void editLink(String shortLink) {
        try {
            if (linkService.isShort(shortLink)) {
                Link link = linkService.getLinkByShortLink(shortLink);
                if (!link.getUUID().equals(this.UUID)) {
                    System.err.println("У Вас нет такой ссылки");
                    return;
                }
                System.out.println("Для редактирования ссылки, скпируйте или перепишите сущестувующие параметры строки, CountLive долежн быть описан в виде 2:30, где 2ч 30 минут");
                System.out.println("LongLink: " + link.getLongLink() + " Limit: " + link.getTransitionLimit() + " CountLive: [0:0]");
                linkService.updateLink(linkService.editLink(link));
            }
        } catch (NotFoundEntityException | TimeErrorException | LimitIsOverException e) {
            System.err.println(e.getMessage());
        }
    }

    private void deleteShortLink(String shortLink) {
        try {
            linkService.deleteShortLink(shortLink, this.UUID);
        } catch (NotFoundEntityException e) {
            System.err.println(e.getMessage());
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
                    "-l [URL]                      - создание короткой ссылки, без параметров.\n" +
                            "-l [URL] [часы:минуты]        - параметр существования ссылки, например: 5:10 где 5 часов, 10 минут.\n" +
                            "-rm [адресс короткой ссылки]  - удаление короткой строки\n" +
                            "all                           - все мои ссылки.\n" +
                            "-e [адресс короткой ссылки]   - редактирование параметров ссылки.\n" +
                            "exit                          - выход");
        } else {
            System.out.println(
                    "[shortUrl]            -  откроет коротку ссылку, если она валидна\n" +
                            "-n [имя пользователя] -  создание нового пользователя\n" +
                            "-u [UUID]             -  вход по UUID\n" +
                            "exit                  -  выход");
        }
    }

    private void authUUID() {
        System.out.println("Введите имя нового пользователя, UUID существующего или уже известную короткую ссылку:");
        while (true) {
            String nameOrUUID = scanner.nextLine();
            String[] line = nameOrUUID.split(" ");
            if (linkService.isShort(line[0])) {
                openShortURL(line[0]);
                continue;
            }
            if (line.length <= 1) {
                if (!line[0].equals("help") && !line[0].equals("exit")) line[0] = "NotFound";
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
            Link link = linkService.getLinkByShortLink(shortURL);
            if (linkService != null) {
                try {
                    if (!link.getUUID().equals(this.UUID)) {
                        link.setTransitionLimit(link.getTransitionLimit() - 1);
                        linkService.updateLink(link);
                    } else {
                        System.out.println("Лимит ссылки не изменен, т.к. Вы являетесь её владельцем.");
                    }
                    Desktop.getDesktop().browse(new URI(link.getLongLink()));
                    System.out.println("Вот Ваша ссылка: " + link.getLongLink());
                } catch (NullPointerException e) {
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
                return true;
            }
        } catch (NotFoundEntityException | TimeErrorException | LimitIsOverException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }
}
