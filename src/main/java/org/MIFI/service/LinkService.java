package org.MIFI.service;

import org.MIFI.GRUD.LinkDAO;
import org.MIFI.entity.Link;
import org.MIFI.entity.Settings;
import org.MIFI.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class LinkService {
    LinkDAO linkDAO;

    public LinkService() {
        this.linkDAO = new LinkDAO();
    }

    public Link addNewLink(String UUID, String longLink, String time) {
        return newLink(UUID, longLink, getTime(time));
    }

    private long getTime(String time) {
        String[] i = time.split(":");
        int countTime = 0;
        try {
            countTime += Integer.parseInt(i[0]) * 1000 * 60 * 60;
            countTime += Integer.parseInt(i[1]) * 1000 * 60;
        } catch (NumberFormatException e) {

        }
        return countTime;
    }

    public Link addNewLink(String UUID, String longLink) {
        return newLink(UUID, longLink, -1);
    }

    private Link newLink(String UUID, String longLink, long DayToEnd) {
        Link link = new Link();
        link.setUUID(UUID);
        link.setLongLink(longLink);
        link.setShortLink(generateNewShortLink(longLink));
        link.setDateStart(new Date().getTime());
        if (DayToEnd <= 0 && Settings.getInstance().getDAYS() < DayToEnd) {
            link.setDateEnd(new Date().getTime() + (Settings.getInstance().getMillisecondsDays()));
        } else {
            link.setDateEnd(new Date().getTime() + DayToEnd);
        }
        link.setTransitionLimit(Settings.getInstance().getLIMIT());
        linkDAO.save(link);
        return link;
    }

    public String getLongLink(String shortLink) {
        String source = "clck.ru/";
        String sourceAndHTTP = "http://clck.ru/";
        if (shortLink.substring(0, 15).equals(sourceAndHTTP)) {
            return linkDAO.findByShortLink(shortLink);
        } else if (shortLink.substring(0, 8).equals(source)) {
            return linkDAO.findByShortLink("http://" + shortLink);
        }
        System.out.println("Проерка не было");
        return null;
    }

    public boolean isShort(String shortLink) {
        String source = "clck.ru/";
        String sourceAndHTTP = "http://clck.ru/";
        try {
            if (shortLink.substring(0, 15).equals(sourceAndHTTP) || shortLink.substring(0, 8).equals(source)) {
                return true;
            }
            return false;
        } catch (StringIndexOutOfBoundsException e) {
            return false;
        }

    }

    public ArrayList<Link> findByUUID(String UUID) {
        return linkDAO.findByUUID(UUID);
    }

    private String generateNewShortLink(String longLink) {
        return "http://clck.ru/" + longLink.substring(1, 5);
    }

    public String getLongString(String shortLink) {
        return linkDAO.findByShortLink(shortLink);
    }

}
