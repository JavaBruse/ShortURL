package org.MIFI;

import org.MIFI.GRUD.ConnectDataBaseUtils;

public class Main {
    public static void main(String[] args) {
        ConnectDataBaseUtils.getInstance().connect();

        System.out.println("Отработано!");
    }
}