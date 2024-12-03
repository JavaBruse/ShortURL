package org.MIFI;

import org.MIFI.service.LinkService;
import org.MIFI.utils.ConfigUtils;
import org.MIFI.utils.DataBaseUtils;

public class Main {
    public static void main(String[] args) {
        DataBaseUtils.getInstance().connect();
        ConfigUtils.getInstance();
        LinkService service = new LinkService();

    }
}