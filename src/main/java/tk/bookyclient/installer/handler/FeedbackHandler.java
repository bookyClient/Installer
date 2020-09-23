package tk.bookyclient.installer.handler;
// Created by booky10 in bookyClientInstaller (22:35 19.09.20)

import tk.bookyclient.installer.utils.Process;

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
        Process.REINSTALLING.set();
        new InstallerThread(true).start();
    }

    public void restart() {
        System.out.println("Restarting...");
        new InstallerThread(false).start();
    }
}