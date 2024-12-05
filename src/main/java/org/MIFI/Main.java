package org.MIFI;

import org.MIFI.utils.ConfigUtils;
import org.MIFI.utils.DataBaseUtils;

public class Main {
    public static void main(String[] args) {
        DataBaseUtils.getInstance().connect();
        ConfigUtils.getInstance();
        CoreApp coreApp = new CoreApp();
        coreApp.start();
    }
}