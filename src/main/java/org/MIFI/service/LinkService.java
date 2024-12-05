package org.MIFI.service;

import org.MIFI.GRUD.LinkDAO;
import org.MIFI.entity.Link;
import org.MIFI.entity.Settings;
import org.MIFI.entity.User;

import java.util.ArrayList;
import java.util.Date;

public class LinkService {
    LinkDAO linkDAO;

    public LinkService() {
        this.linkDAO = new LinkDAO();
    }

    public Link addNewLink(User user, String longLink, int DayToEnd) {
        return newLink(user, longLink, DayToEnd);
    }

    public Link addNewLink(User user, String longLink) {
        return newLink(user, longLink, -1);
    }

    private Link newLink(User user, String longLink, int DayToEnd) {
        Link link = new Link();
        link.setUUID(user.getUUID());
        link.setLongLink(longLink);
        link.setShortLink(generateNewShortLink(longLink));
        link.setDateStart(new Date().getTime());
        if (DayToEnd <= 0 && Settings.getInstance().getDAYS() < DayToEnd) {
            link.setDateEnd(new Date().getTime() + (Settings.getInstance().getMillisecondsDays()));
        } else {
            link.setDateEnd(new Date().getTime() + ((long) DayToEnd * 24 * 60 * 60 * 1000));
        }
        link.setTransitionLimit(Settings.getInstance().getLIMIT());
        linkDAO.save(link);
        return link;
    }

    public String getLongLink(String shortLink){
        return linkDAO.findByShortLink(shortLink);
    }

    public ArrayList<Link> findByUUID(String UUID) {
        return linkDAO.findByUUID(UUID);
    }

    private String generateNewShortLink(String longLink) {
        return "https://clck.ru/" + longLink.substring(1, 5);
    }

    public String getLongString(String shortLink) {
        return linkDAO.findByShortLink(shortLink);
    }
}
