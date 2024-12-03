package org.MIFI.entity;

import lombok.Data;

@Data
public class Settings {
    int LinksToLifetime;
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
