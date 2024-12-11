package org.MIFI;

import org.MIFI.utils.ConfigUtils;
import org.MIFI.utils.DataBaseUtils;

import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) {
        DataBaseUtils.getInstance().connect();
        ConfigUtils.getInstance();
        ConsoleInterface consoleInterface = new ConsoleInterface();
        try {
            consoleInterface.start();
        } catch (NoSuchElementException e){
        }
    }
}