package tk.bookyclient.installer.utils;
// Created by booky10 in bookyClientInstaller (14:32 20.09.20)

public class Utils {

    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored) {
        }
    }

    public static String buildString(String[] strings, Character seperator, int indexFrom) {
        return buildString(strings, seperator, indexFrom, strings.length - 1);
    }

    public static String buildString(String[] strings, Character seperator, int indexFrom, int indexTo) {
        StringBuilder builder = new StringBuilder();
        for (int i = indexFrom; i < indexTo + 1; i++) {
            builder.append(strings[i]);
            if (seperator != null && i != indexTo) builder.append(seperator);
        }
        return builder.toString();
    }

    public static void setBarPercentage(int percentage) {

    }
}