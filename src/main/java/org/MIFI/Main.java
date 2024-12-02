package org.MIFI;

public class Main {
    public static void main(String[] args) {
        DataBaseUtils.getInstance().connect();

        System.out.println("Отработано!");
    }
}