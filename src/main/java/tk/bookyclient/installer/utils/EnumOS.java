package tk.bookyclient.installer.utils;
// Created by booky10 in bookyClientInstaller (21:14 19.09.20)

import java.io.File;

public enum EnumOS {

    LINUX(".minecraft"),
    SOLARIS(".minecraft"),
    WINDOWS("AppData" + File.separator + "Roaming" + File.separator + ".minecraft"),
    OSX("Library" + File.separator + "Application Support" + File.separator + "minecraft"),
    UNKNOWN(null);

    private final String directory;
    private static EnumOS os = null;

    EnumOS(String directory) {
        this.directory = directory;
    }

    public File getDirectory() {
        return new File(System.getProperty("user.home"), directory);
    }

    public static EnumOS getOS() {
        if (os == null) {
            String currentOS = System.getProperty("os.name").toLowerCase();
            EnumOS os = EnumOS.UNKNOWN;

            if (currentOS.contains("win")) os = EnumOS.WINDOWS;
            else if (currentOS.contains("mac")) os = EnumOS.OSX;
            else if (currentOS.contains("solaris") || currentOS.contains("sunos")) os = EnumOS.SOLARIS;
            else if (currentOS.contains("linux") || currentOS.contains("unix")) os = EnumOS.LINUX;

            EnumOS.os = os;
        }
        return os;
    }
}