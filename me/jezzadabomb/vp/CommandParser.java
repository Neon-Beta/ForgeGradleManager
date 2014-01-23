package me.jezzadabomb.vp;

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
            Runtime.getRuntime().exec("cmd /c start " + command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
