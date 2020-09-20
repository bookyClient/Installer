package tk.bookyclient.installer.handler;
// Created by booky10 in bookyClientInstaller (22:35 19.09.20)

/**
 * Called from JavaScript
 */
public class FeedbackHandler {

    public void close() {
        System.out.println("Closing...");
        System.exit(0);
    }

    public void reinstall() {
        System.out.println("Reinstalling...");
        new InstallerThread(true).start();
    }
}