package org.MIFI.entity;

import lombok.Data;

@Data
public class Settings {
    int LinksToLifetime;
    long DaysLiveURL = 24 * 60 * 60 * 1000;
    private static volatile Settings instance;

    public static Settings getInstance() {
        Settings localInstance = instance;
        if (localInstance == null) {
            synchronized (Settings.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Settings();
                }
            }
        }
        return localInstance;
    }

}
