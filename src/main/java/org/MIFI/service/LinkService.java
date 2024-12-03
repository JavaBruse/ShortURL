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

    public void addNewLink(User user, String longLink, int TimeToEnd) {
        Link link = new Link();
        link.setUUID(user.getUUID());
        link.setLongLink(longLink);
        link.setShortLink(generateNewShortLink(longLink));
        link.setDateStart(new Date().getTime());
        link.setDateEnd(new Date().getTime() + ((long) TimeToEnd * 24 * 60 * 60 * 1000));
        link.setTransitionLimit(Settings.getInstance().getLinksToLifetime());
        linkDAO.save(link);
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
