package org.MIFI.utils;

import org.MIFI.entity.Settings;
import org.ini4j.Wini;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConfigUtils {
    private Wini ini;
    private final String INI_FILE = "config.ini";

    public ConfigUtils() {
        iniWrite();
        if (!checkFileIni()) {
            recordSettings();
        } else {
            extractSettings();
        }
    }

    public void iniWrite() {
        try {
           this.ini = new Wini(getFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static volatile ConfigUtils instance;

    public static ConfigUtils getInstance() {
        ConfigUtils localInstance = instance;
        if (localInstance == null) {
            synchronized (ConfigUtils.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ConfigUtils();
                }
            }
        }
        return localInstance;
    }


    public void recordSettings() {
        if (checkFileIni()) {
            ini.put("Settings", "limit", Settings.getInstance().getLIMIT());
            ini.put("Settings", "Life_day", Settings.getInstance().getDAYS());
            updateIniFile();
        }
    }

    public void extractSettings() {
        if (checkFileIni()) {
            String LIMIT = this.ini.get("Settings",  "limit");
            String TIMEtoLIFE = this.ini.get("Settings", "Life_day");
            Settings.getInstance().setLIMIT(Integer.parseInt(LIMIT));
            Settings.getInstance().setDAYS(Integer.parseInt(TIMEtoLIFE));
        }
    }


    private void updateIniFile() {
        try {
            ini.store(new FileOutputStream(INI_FILE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File getFile() {
        File file = new File(INI_FILE);
        if (!checkFileIni()) {
            try {
                file.createNewFile();
                this.ini = new Wini(file);
                recordSettings();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return file;
    }

    private boolean checkFileIni() {
        File file = new File(INI_FILE);
        if (!file.exists()) {
            return false;
        }
        return true;
    }
}
