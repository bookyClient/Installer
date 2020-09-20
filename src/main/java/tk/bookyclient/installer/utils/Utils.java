package tk.bookyclient.installer.utils;
// Created by booky10 in bookyClientInstaller (14:32 20.09.20)

public class Utils {

    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored) {
        }
    }
}