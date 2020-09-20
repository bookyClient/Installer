package tk.bookyclient.installer.handler;
// Created by booky10 in bookyClientInstaller (18:15 20.09.20)

import javafx.application.Platform;
import tk.bookyclient.installer.Boot;
import tk.bookyclient.installer.utils.ClientDownloader;

import java.util.Timer;
import java.util.TimerTask;

public class PercentageTask extends TimerTask {

    public static final Long PERIOD = 125L;

    public static void runTimer() {
        new Timer().schedule(new PercentageTask(), PERIOD, PERIOD);
    }

    @Override
    public void run() {
        if (!InstallerThread.installing) cancel();
        Platform.runLater(() -> Boot.instance.webView.getEngine().executeScript("javascript:setBarPercentage("
                + ClientDownloader.percentage + ", \"" + ClientDownloader.downloadStage + "\")"));
    }
}