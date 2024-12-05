package org.MIFI.entity;

import lombok.Data;

@Data
public class Settings {
    int LIMIT = 3;
    int DAYS = 10;
    long MillisecondsDays = DAYS * 24 * 60 * 60 * 1000;
    private static volatile Settings instance;

    public void setDAYS(int DAYS) {
        this.DAYS = DAYS;
        this.MillisecondsDays = DAYS * 24 * 60 * 60 * 1000;
    }

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
