package org.MIFI.service;

import org.MIFI.GRUD.LinkDAO;
import org.MIFI.entity.Link;
import org.MIFI.entity.Settings;
import org.MIFI.exceptions.NotFoundEntityException;
import org.MIFI.exceptions.TimeErrorException;
import org.MIFI.exceptions.URLNotAvailable;
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

    public Link addNewLink(String UUID, String longLink, String time) throws TimeErrorException, URLNotCorrect {
        try {
            Link link = newLink(UUID, longLink, getTime(time));
            return link;
        } catch (TimeErrorException | URLNotCorrect e) {
            throw e;
        }

    }

    private long getTime(String time) throws TimeErrorException {
        int countTime = 0;
        String[] i = time.split(":");
        try {
            countTime += Integer.parseInt(i[0]) * 1000 * 60 * 60;
            countTime += Integer.parseInt(i[1]) * 1000 * 60;
        } catch (RuntimeException e) {
            throw new TimeErrorException("Некорректно задано время, ошибка: " + time);
        }
        return countTime;
    }

    public Link addNewLink(String UUID, String longLink) throws URLNotCorrect {
        try {
            return newLink(UUID, longLink, 0);
        } catch (URLNotCorrect e) {
            throw e;
        }

    }

    private Link newLink(String UUID, String longLink, long DayToEnd) throws URLNotCorrect {
        if (!isURL(longLink)) throw new URLNotCorrect("Адресс не корректен!");
        Link link = new Link();
        link.setUUID(UUID);
        link.setLongLink(longLink);
        link.setShortLink(generateNewShortLink(longLink));
        link.setDateStart(new Date().getTime());
        if ((DayToEnd > 0 && Settings.getInstance().getDAYS() > DayToEnd) || DayToEnd == 0) {
            link.setDateEnd(new Date().getTime() + (Settings.getInstance().getMillisecondsDays()));
        } else {
            link.setDateEnd(new Date().getTime() + DayToEnd);
        }
        link.setTransitionLimit(Settings.getInstance().getLIMIT());
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

    public Link getLongLink(String shortLink) {
        try {
            if (shortLink.substring(0, 15).equals(sourceAndHTTP)) {
                return findLongLink(shortLink);
                //return linkDAO.findLongByShortLink(shortLink);
            } else if (shortLink.substring(0, 8).equals(source)) {
                return findLongLink("http://" + shortLink);
                //return linkDAO.findLongByShortLink("http://" + shortLink);
            }
        } catch (NotFoundEntityException e) {
            System.err.println(e);
        }
        return null;
    }

    public boolean isShort(String shortLink) {
        try {
            if (shortLink.substring(0, 15).equals(sourceAndHTTP) || shortLink.substring(0, 8).equals(source)) {
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

    private String generateNewShortLink(String longLink) {
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
        String urlPattern = "^(https?|ftp)://[\\w-]+(\\.[\\w-]+)+(:\\d+)?(/[\\w-./?%&=]*)?$";
        return Pattern.matches(urlPattern, URL);
    }

    private boolean isAvailable(String url) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("HEAD");
        return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
    }
}
