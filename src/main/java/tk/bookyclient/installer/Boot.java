package tk.bookyclient.installer;
// Created by booky10 in bookyClientInstaller (20:47 19.09.20)

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import tk.bookyclient.installer.handler.FeedbackHandler;
import tk.bookyclient.installer.handler.InstallerThread;
import tk.bookyclient.installer.utils.SharedConstants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Boot extends Application {

    public static final String VERSION = "v1.2";

    public static void main(String[] args) {
        System.out.println("Launching bookyClientInstall " + VERSION + "...");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (InstallerThread.installing) {
                System.err.println("Still installing! Deleting files...");
                try {
                    File[] files = SharedConstants.CLIENT_FOLDER.listFiles();
                    if (files != null) for (File file : files) Files.deleteIfExists(file.toPath().toAbsolutePath());
                    Files.deleteIfExists(SharedConstants.CLIENT_FOLDER.toPath().toAbsolutePath());
                } catch (IOException ignored) {
                }
            }
            System.out.println("Exiting!");
        }, "Shutdown Thread"));
        Application.launch(Boot.class);

        System.exit(0);
    }

    public static Boot instance;
    private final FeedbackHandler feedbackHandler = new FeedbackHandler();
    public final WebView webView = new WebView();

    @Override
    public void start(Stage stage) {
        instance = this;

        VBox layout = new VBox();
        layout.getChildren().add(webView);

        stage.setScene(new Scene(layout));
        stage.setTitle("bookyClient Installer");
        stage.getIcons().add(new Image(SharedConstants.getIcon()));
        stage.setResizable(false);
        stage.setWidth(1063);
        stage.setHeight(620);
        webView.setContextMenuEnabled(false);

        Platform.runLater(() -> {
            webView.getEngine().load(ClassLoader.getSystemResource("index.html").toExternalForm());
            webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == Worker.State.SUCCEEDED) {
                    ((JSObject) webView.getEngine().executeScript("window")).setMember("feedback", feedbackHandler);
                    if (webView.getEngine().getLocation().toLowerCase().contains("index.html"))
                        new InstallerThread(false).start();
                }
            });
        });
        stage.show();
        System.out.println("Launched!");
    }
}