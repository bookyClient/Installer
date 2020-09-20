package tk.bookyclient.installer.utils;
// Created by booky10 in bookyClientInstaller (22:40 19.09.20)

import javafx.application.Platform;
import tk.bookyclient.installer.Boot;

public enum Process {

    STARTING(null),
    INSTALLING("startInstall"),
    FINISHED("finished"),
    ERRORED("errored"),
    ALREADY_INSTALLED("alreadyInstalled");

    private final String functionName;

    Process(String functionName) {
        this.functionName = functionName;
    }

    public void set(String... args) {
        if (functionName == null) return;

        Boot.instance.process = this;
        String formattedArgs = String.join(", ", args);
        Platform.runLater(() -> Boot.instance.webView.getEngine().executeScript("javascript:" + functionName + "(" + formattedArgs + ")"));
    }
}