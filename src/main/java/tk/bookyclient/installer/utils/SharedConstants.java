package tk.bookyclient.installer.utils;
// Created by booky10 in bookyClientInstaller (21:20 19.09.20)

import javafx.scene.media.Media;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class SharedConstants {

    public static final String LAUNCHER_ICON = getLauncherIcon();
    public static final EnumOS OS = EnumOS.getOS();
    public static final String VERSION_AND_DOWNLOAD = getLatestVersionAndDownload();
    public static final File VERSIONS_FOLDER = new File(SharedConstants.OS.getDirectory(), "versions");
    public static final File CLIENT_FOLDER = new File(VERSIONS_FOLDER, "bookyClient");

    public static final String VERSIONS_URL = "https://bookyclient.tk/versions-189.txt";
    public static final String ICON_URL = "https://bookyclient.tk/launcher-logo.txt";
    public static final String JSON_URL = "https://bookyclient.tk/launcher-json.json";

    public static String getLauncherIcon() {
        try {
            URL url = new URL(ICON_URL);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "bookyClientInstaller/1.0");
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String icon = reader.readLine();

            reader.close();
            connection.disconnect();

            return icon;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return "TNT";
        }
    }

    public static String getLatestVersionAndDownload() {
        try {
            URL url = new URL(VERSIONS_URL);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "bookyClientInstaller/1.0");
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String version = reader.readLine();

            reader.close();
            connection.disconnect();

            return version;
        } catch (Throwable throwable) {
            return "NaN:NaN";
        }
    }

    public static InputStream getIcon() {
        return ClassLoader.getSystemResourceAsStream("assets/logoBlack.png");
    }

    public static Media getAudio() {
        try {
            return new Media(ClassLoader.getSystemResource("assets/audio.mp3").toURI().toString());
        } catch (Throwable throwable) {
            throw new Error(throwable);
        }
    }
}
