package tk.bookyclient.installer.utils;
// Created by booky10 in bookyClientInstaller (21:50 19.09.20)

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import tk.bookyclient.installer.Boot;
import tk.bookyclient.installer.exceptions.NoLinkException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class ClientDownloader {

    public static Double percentage = 0D;
    public static String downloadStage = "";
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);

    static {
        timeFormat.setTimeZone(Calendar.getInstance().getTimeZone());
    }

    public static void downloadJSON() {
        try {
            createDirectory();
            File target = new File(SharedConstants.CLIENT_FOLDER, "bookyClient.json");

            downloadStage = "Downloading JSON...";
            download(new URL(SharedConstants.JSON_URL), target);
        } catch (MalformedURLException exception) {
            throw new Error(exception);
        }
    }

    public static void downloadJAR() {
        createDirectory();
        File target = new File(SharedConstants.CLIENT_FOLDER, "bookyClient.jar");
        String url = SharedConstants.VERSION_AND_DOWNLOAD.split(";")[1];

        if (url.equals("NaN")) throw new NoLinkException();
        downloadStage = "Downloading JAR...";
        download(url, target);
    }

    public static JsonObject getNewProfile() {
        JsonObject profile = new JsonObject();
        profile.addProperty("created", timeFormat.format(new Date(System.currentTimeMillis())));
        profile.addProperty("icon", SharedConstants.LAUNCHER_ICON);
        profile.addProperty("javaArgs", "-Xmx3G -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 -XX:MaxGCPauseMillis=50 -XX:G1HeapRegionSize=32M -Duser.country=US -Duser.language=en");
        profile.addProperty("lastUsed", timeFormat.format(new Date(System.currentTimeMillis())));
        profile.addProperty("lastVersionId", "bookyClient");
        profile.addProperty("name", "bookyClient");
        profile.addProperty("type", "custom");
        return profile;
    }

    public static void installNewProfile() {
        downloadStage = "Installing Profile...";
        percentage = 0D;
        File profilesFile = new File(EnumOS.getOS().getDirectory(), "launcher_profiles.json");
        if (!profilesFile.exists()) throw new IllegalStateException("Profiles File does not exits!");

        try (FileReader reader = new FileReader(profilesFile)) {
            JsonObject parsed = (JsonObject) JsonParser.parseReader(reader);
            JsonObject profiles = new JsonObject();

            if (parsed.has("profiles")) {
                profiles = parsed.getAsJsonObject("profiles");
                parsed.remove("profiles");
            }

            profiles.add(getTrimmedUUID(), getNewProfile());
            parsed.add("profiles", profiles);

            Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
            String output = gson.toJson(parsed);

            try (FileWriter writer = new FileWriter(profilesFile)) {
                try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
                    bufferedWriter.write(output);
                }
            }
        } catch (IOException exception) {
            throw new Error(exception);
        } finally {
            percentage = 100D;
        }
    }

    private static String getTrimmedUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static Boolean isInstalled() {
        return SharedConstants.CLIENT_FOLDER.exists();
    }

    public static void createDirectory() {
        SharedConstants.CLIENT_FOLDER.mkdirs();
    }

    private static void download(String url, File file) {
        try {
            download(new URL(url), file);
        } catch (MalformedURLException exception) {
            throw new Error(exception);
        }
    }

    private static void download(URL url, File file) {
        percentage = 0D;
        BufferedInputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "bookyClientInstaller/" + Boot.VERSION);
            int size = connection.getContentLength();

            if (size < 0)
                System.err.println("Could not get the file size");
            else
                System.out.println("File size: " + size);

            connection.connect();
            inputStream = new BufferedInputStream(connection.getInputStream());
            outputStream = new FileOutputStream(file);
            byte[] data = new byte[1024];
            int count;
            double sumCount = 0.0;

            while ((count = inputStream.read(data, 0, 1024)) != -1) {
                outputStream.write(data, 0, count);

                sumCount += count;
                if (size > 0) percentage = sumCount / size * 100.0;
            }
        } catch (IOException exception) {
            throw new Error(exception);
        } finally {
            percentage = 100D;
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            } catch (IOException exception) {
                throw new Error(exception);
            }
        }
    }
}