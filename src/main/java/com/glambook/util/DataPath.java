package com.glambook.util;

import java.io.File;

/**
 * Keeps the location of the folder that holds all the .txt data files.
 *
 * Default folder: <user home>/glambook-data
 * It can be changed with the JVM option -Dglambook.data.dir=C:/path/to/folder
 * or with the context-param "glambook.data.dir" in web.xml.
 */
public class DataPath {

    public static final String PROPERTY_NAME = "glambook.data.dir";

    private static String dataDir = System.getProperty("user.home") + File.separator + "glambook-data";

    // utility class, so no objects are needed
    private DataPath() {
    }

    public static String getDataDir() {
        return dataDir;
    }

    // only change the folder if a real value was given
    public static void setDataDir(String dir) {
        if (dir != null && !dir.trim().isEmpty()) {
            dataDir = dir.trim();
        }
    }

    // returns the full path of one data file, e.g. users.txt
    public static File getFile(String fileName) {
        return new File(dataDir, fileName);
    }
}
