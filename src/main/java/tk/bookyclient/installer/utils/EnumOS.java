package tk.bookyclient.installer.utils;
// Created by booky10 in bookyClientInstaller (21:14 19.09.20)

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public enum EnumOS {

    LINUX("ps -e", "minecraft-launc", ".minecraft"),
    SOLARIS("ps -e", "minecraft-launc", ".minecraft"),
    WINDOWS("tasklist", "MinecraftLauncher.exe", "AppData" + File.separator + "Roaming" + File.separator + ".minecraft"),
    OSX("ps -e -o command", "minecraft-launc", "Library" + File.separator + "Application Support" + File.separator + "minecraft"),
    UNKNOWN(null, null, null);

    private final String taskListCommand, launcherProcess, directory;
    private static EnumOS os = null;

    EnumOS(String taskListCommand, String launcherProcess, String directory) {
        this.taskListCommand = taskListCommand;
        this.launcherProcess = launcherProcess;
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

    public static boolean isLauncherStarted() {
        try {
            java.lang.Process execution = Runtime.getRuntime().exec(os.taskListCommand);
            BufferedReader reader = new BufferedReader(new InputStreamReader(execution.getInputStream()));
            List<String> processes = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) processes.add(line);
            for (String process : processes) if (process.toLowerCase().contains(os.launcherProcess.toLowerCase())) return true;

            return false;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return false;
        }
    }
}