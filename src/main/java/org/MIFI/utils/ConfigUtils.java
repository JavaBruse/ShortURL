package org.MIFI.utils;

import org.MIFI.entity.Settings;
import org.ini4j.Wini;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConfigUtils {
    private Wini ini;
    private final String INI_FILE = "config.ini";
    private boolean getIsDir = checkFileIni();

    public ConfigUtils() {
        if (getIsDir) {
            iniWrite();
            extractSettings();

        }
    }

    public void iniWrite() {
        try {
            ini = new Wini(getFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setGetIsDir(boolean getIsDir) {
        this.getIsDir = getIsDir;
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
        if (getIsDir) {
            ini.put("Settings", "LinksToLifetime", Settings.getInstance().getLinksToLifetime());
            updateIniFile();
        }
    }

    public void extractSettings() {
        if (getIsDir) {
            String settings = ini.get("Settings", "\n" +
                    "LinksToLifetime");
            if (settings != null) {
                try {
                    Settings.getInstance().setLinksToLifetime(Integer.parseInt(settings));
                } catch (Exception e) {
                    return;
                }
            }
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
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
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
