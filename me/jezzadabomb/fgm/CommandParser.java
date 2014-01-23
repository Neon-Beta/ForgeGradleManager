package me.jezzadabomb.fgm;

import java.io.IOException;

public class CommandParser {
    ForgeGradleManager fgm;

    public CommandParser(ForgeGradleManager fgm) {
        this.fgm = fgm;
    }

    public void parseGradleCommand(String command) {
        runCommand(fgm.utils.getGradlewBatch() + " " + command);
    }

    public void runCommand(String command) {
        if (fgm.isDebugMode())
            return;
        try {
            String osName = System.getProperty("os.name");
            if (!osName.toLowerCase().startsWith("windows")) {
                Runtime.getRuntime().exec("cmd /c start " + command);
            } else {
                Runtime.getRuntime().exec(command);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
