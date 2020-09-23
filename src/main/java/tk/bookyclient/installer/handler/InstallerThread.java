package tk.bookyclient.installer.handler;
// Created by booky10 in bookyClientInstaller (23:00 19.09.20)

import javafx.scene.media.MediaPlayer;
import tk.bookyclient.installer.exceptions.NoLinkException;
import tk.bookyclient.installer.utils.ClientDownloader;
import tk.bookyclient.installer.utils.Process;
import tk.bookyclient.installer.utils.SharedConstants;
import tk.bookyclient.installer.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class InstallerThread extends Thread {

    private final MediaPlayer player = new MediaPlayer(SharedConstants.getAudio());
    private final Boolean reinstall;
    public static Boolean installing = false;

    public InstallerThread(Boolean reinstall) {
        this.reinstall = reinstall;
    }

    @Override
    public void run() {
        boolean success = false;
        try {
            Utils.sleep(2000);

            if (ClientDownloader.isInstalled() && !reinstall) {
                System.out.println("Already installed!");
                Process.ALREADY_INSTALLED.set();
            } else if (ClientDownloader.isLauncherOpen()) {
                System.out.println("Launcher open!");
                Process.LAUNCHER_OPEN.set();
            } else {
                installing = true;
                if (ClientDownloader.isInstalled() && reinstall) {
                    System.out.println("Deleting old Files...");
                    File[] files = SharedConstants.CLIENT_FOLDER.listFiles();
                    if (files != null) for (File file : files) Files.deleteIfExists(file.toPath().toAbsolutePath());
                    Files.deleteIfExists(SharedConstants.CLIENT_FOLDER.toPath().toAbsolutePath());
                }

                System.out.println("Installing...");
                Process.INSTALLING.set();
                PercentageTask.runTimer();

                player.play();
                player.setOnEndOfMedia(player::play);
                player.setVolume(0.25);

                System.out.println("Downloading JSON...");
                ClientDownloader.downloadJSON();
                System.out.println("Downloading JAR...");
                ClientDownloader.downloadJAR();
                System.out.println("Installing Profile...");
                ClientDownloader.installNewProfile();

                System.out.println("Finished!");
                Process.FINISHED.set();

                installing = false;
            }
            success = true;
        } catch (NoLinkException exception) {
            System.out.println("No Link available!");
            Utils.sleep(1000);
            Process.ERRORED.set("\"Der Link zum Download tut im Moment nicht!\"");

            installing = false;
        } catch (Throwable throwable) {
            Utils.sleep(1000);
            throwable.printStackTrace();
            Process.ERRORED.set("\"" + throwable.getMessage() + "\"");

            installing = false;
        } finally {
            while (player.getVolume() > 0) {
                player.setVolume(player.getVolume() - 0.0125);
                Utils.sleep(150);
            }
            player.stop();

            if (!success) try {
                File[] files = SharedConstants.CLIENT_FOLDER.listFiles();
                if (files != null) for (File file : files) Files.deleteIfExists(file.toPath().toAbsolutePath());
                Files.deleteIfExists(SharedConstants.CLIENT_FOLDER.toPath().toAbsolutePath());
            } catch (IOException ignored) {
            }
        }
    }
}