package org.MIFI.service;

import org.MIFI.GRUD.LinkDAO;
import org.MIFI.entity.Link;
import org.MIFI.entity.Settings;
import org.MIFI.exceptions.LimitIsOverException;
import org.MIFI.exceptions.NotFoundEntityException;
import org.MIFI.exceptions.TimeErrorException;
import org.MIFI.exceptions.URLNotCorrect;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

public class LinkService {
    LinkDAO linkDAO;

    String source = "clck.ru/";
    String sourceAndHTTP = "http://clck.ru/";

    public LinkService() {
        this.linkDAO = new LinkDAO();
    }

    String errorLink = "http://example.com минимальный URL\n" +
            "http://127.0.0.1 с IP вместо доменного имени\n" +
            "http://example.com:8080 с портом\n" +
            "http://example.com/path/to/resource с путем\n" +
            "http://example.com?param1=value1&param2=value2 с query-параметрами\n" +
            "http://example.com#top с якорем\n" +
            "https://example.com:443/path/to/resource?key=value#section полный URL";

    public Link addNewLink(String UUID, String longLink, String time) throws TimeErrorException, URLNotCorrect {
        try {
            Long countTime;
            countTime = getTime(time);
            if ((countTime > 0 && Settings.getInstance().getMillisecondsDays() < countTime)) {
                countTime = Settings.getInstance().getMillisecondsDays();
            } else if (countTime == 0) {
                countTime = Settings.getInstance().getMillisecondsDays();
            }
            Link link = newLink(UUID, longLink, countTime);
            return link;
        } catch (TimeErrorException | URLNotCorrect e) {
            throw e;
        }

    }

    public Link editLink(Link link) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String[] line = scanner.nextLine().split(" ");
            try {
                if (line.length == 6) {
                    if (isURL(line[1])) {
                        link.setLongLink(line[1]);
                        try {
                            if (Integer.parseInt(line[3]) > Settings.getInstance().getLIMIT()) {
                                link.setTransitionLimit(Settings.getInstance().getLIMIT());
                            } else {
                                link.setTransitionLimit(Integer.parseInt(line[3]));
                            }
                        } catch (NumberFormatException e) {
                            throw new NotFoundEntityException("Не корректен лимит переходов: " + line[3]);
                        }

                        Long time = getTime(line[5]);
                        if (time > Settings.getInstance().getMillisecondsDays()) {
                            link.setDateEnd(new Date().getTime() + Settings.getInstance().getMillisecondsDays());
                        } else {
                            link.setDateEnd(new Date().getTime() + time);
                        }
                        System.out.println("Link обновлен:\n" + link);
                        return link;
                    } else {
                        throw new URLNotCorrect("LongLink не корректен, это не ссылка!");
                    }
                } else {
                    throw new NotFoundEntityException("Не корректная строка, проверьте введенные значение, их меньше 6");
                }
            } catch (NumberFormatException | TimeErrorException | URLNotCorrect | NotFoundEntityException e) {
                System.err.println(e.getMessage());
            }

        }
    }

    private long getTime(String time) throws TimeErrorException {
        Long countTime = 0l;
        String[] i = time.split(":");
        try {
            countTime += Long.parseLong(i[0]) * 1000l * 60l * 60l;
            countTime += Long.parseLong(i[1]) * 1000l * 60l;
        } catch (RuntimeException e) {
            throw new TimeErrorException("Некорректно задано время, ошибка: " + time);
        }
        return countTime;
    }

    public Link addNewLink(String UUID, String longLink) throws URLNotCorrect {
        try {
            return newLink(UUID, longLink, Settings.getInstance().getMillisecondsDays());
        } catch (URLNotCorrect e) {
            throw e;
        }
    }

    private Link newLink(String UUID, String longLink, Long DayToEnd) throws URLNotCorrect {
        if (!isURL(longLink)) throw new URLNotCorrect("Адресс не корректен, вот варианты:\n" + errorLink);
        Link link = new Link();
        link.setUUID(UUID);
        link.setLongLink(longLink);
        link.setShortLink(generateNewShortLink());
        link.setDateStart(new Date().getTime());
        link.setDateEnd(new Date().getTime() + DayToEnd);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите лимит переходов, максимум указаный в конфиге: " + Settings.getInstance().getLIMIT());
        int limit = 0;
        while (true) {

            try {
                limit = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.err.println("Не корректное значение, введите цифру от 1, или 0 это значение по умолчанию из конфига");
            }
        }

        if (limit == 0 || limit > Settings.getInstance().getLIMIT()) {
            link.setTransitionLimit(Settings.getInstance().getLIMIT());
        } else {
            link.setTransitionLimit(limit);
        }
        linkDAO.saveLink(link);
        return link;
    }

    public ArrayList<Link> findByUUID(String UUID) throws NotFoundEntityException {
        Optional<ArrayList<Link>> optionalLinks = linkDAO.findByUUID(UUID);
        if (optionalLinks.isPresent()) {
            return optionalLinks.get();
        } else {
            throw new NotFoundEntityException("Ссылки по Вашему UUID: " + UUID + ", не найдено");
        }
    }

    public Link getLinkByShortLink(String shortLink) {
        try {
            Link link = null;
            if (shortLink.substring(0, 8).equals(source)) {
                link = findLongLink("http://" + shortLink);

            } else if (shortLink.substring(0, 15).equals(sourceAndHTTP)) {
                link = findLongLink(shortLink);
            }
            if (link.getDateEnd() < new Date().getTime()) {
                linkDAO.deleteById(link.getId());
                throw new TimeErrorException("Время активности ссылки истекло, ссылка удалена!");
            }
            if (link.getTransitionLimit() <= 0) {
                linkDAO.deleteById(link.getId());
                throw new LimitIsOverException("Лимит на переходы истек, ссылка удалена!");
            }
            return link;
        } catch (NotFoundEntityException | NullPointerException e) {
            System.err.println(e);
        }
        return null;
    }


    public boolean isShort(String shortLink) {
        try {
            if (shortLink.substring(0, 8).equals(source) || shortLink.substring(0, 15).equals(sourceAndHTTP)) {
                return true;
            }
            return false;
        } catch (StringIndexOutOfBoundsException e) {
            return false;
        }

    }

    public boolean updateLink(Link link) {
        return linkDAO.updateLink(link);
    }

    private Link findLongLink(String shortLink) throws NotFoundEntityException {
        Optional<Link> optionalLink = linkDAO.findLongByShortLink(shortLink);
        if (optionalLink.isPresent()) {
            return optionalLink.get();
        } else {
            throw new NotFoundEntityException("Нет такой ссылки: " + shortLink);
        }
    }

    private String generateNewShortLink() {
        String dict = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890"; //строка содержит все доступные символы
        StringBuilder sb = new StringBuilder("http://clck.ru/");
        int max = dict.length() - 1;
        int min = 0;
        while (true) {
            for (int i = 0; i < 6; i++) {
                int number = randomMinMax(min, max);
                sb.append(dict.substring(number, number + 1));
            }
            Optional<Link> optionalLink = linkDAO.findLongByShortLink(sb.toString());
            if (!optionalLink.isPresent()) {
                return sb.toString();
            }
        }
    }

    private static int randomMinMax(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

    private boolean isURL(String URL) {
//        String urlPattern = "^(https?|ftp)://[\\w-]+(\\.[\\w-]+)+(:\\d+)?(/[\\w-./?%&=]*)?$";
        String urlPattern = "^(https?|ftp)://((([\\w-]+\\.)+[\\w-]+)|(\\d{1,3}(\\.\\d{1,3}){3}))(:\\d+)?(/[\\w-./:@!$&'()*+,;=~%]*)?(\\?[\\w-./:@!$&'()*+,;=~%]*)?(#[\\w-./:@!$&'()*+,;=~%]*)?$";
        return Pattern.matches(urlPattern, URL);
    }

    private boolean isAvailable(String url) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("HEAD");
        return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
    }

    public void deleteShortLink(String shortLink, String UUID) {
        try {
            ArrayList<Link> links = findByUUID(UUID);
            Long id = links.stream().filter(link -> link.getShortLink().equals(shortLink)).findFirst().get().getId();
            linkDAO.deleteById(id);
            System.out.println(shortLink + " удалена");
        } catch (NoSuchElementException e) {
            throw new NotFoundEntityException("Ссылка: " + shortLink + " не наедена!");
        }
    }
}
