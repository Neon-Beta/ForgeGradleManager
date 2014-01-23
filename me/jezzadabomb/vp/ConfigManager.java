package me.jezzadabomb.vp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ConfigManager {

    private String configFile = "FGM.cfg";
    private ForgeGradleManager fgm;

    public ConfigManager(ForgeGradleManager fgm) {
        this.fgm = fgm;
    }

    public void confirmConfigFile() {
        File config = new File(fgm.utils.getWorkingDirectory() + configFile);
        if (!config.exists()) {
            try {
                config.createNewFile();
            } catch (IOException e) {
                fgm.utils.errorBox("Failed to read config.");
                defaultConfigs();
                e.printStackTrace();
            }
        }
    }

    public void readFromConfig() {
        ArrayList<String> file = new ArrayList<String>();

        confirmConfigFile();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fgm.utils.getWorkingDirectory() + configFile));
            String line = null;
            while ((line = reader.readLine()) != null)
                if (!line.startsWith("#"))
                    file.add(line);
            reader.close();
        } catch (IOException e) {
            defaultConfigs();
            fgm.utils.errorBox("Something wrong with config file." + System.lineSeparator() + "Tip: you can delete it if you need.");
        }
        for (String line : file) {
            if (line.startsWith("secondary:")) {
                fgm.setSecondaryFile(Boolean.parseBoolean(line.substring(line.lastIndexOf(" ") + 1)));
            } else if (line.startsWith("cleanBuild:")) {
                fgm.setCleanBuild(Boolean.parseBoolean(line.substring(line.lastIndexOf(" ") + 1)));
            } else if (line.startsWith("batchFile:")) {
                String temp = line.substring(line.indexOf(":") + 2);
                if (temp.equals("blank") || !temp.endsWith(".bat")) {
                    fgm.setSecondaryFile(false);
                    fgm.utils.setBatchFile("");
                    return;
                }
                fgm.utils.setBatchFile(temp);
                fgm.setBatchFileName(temp);
            }
        }
    }

    @SuppressWarnings("serial")
    public void saveToConfig() {

        confirmConfigFile();

        final String batchFile = fgm.utils.getBatchFile();

        ArrayList<String> configFile = new ArrayList<String>() {
            {
                add("#Don't touch this. Unless you know what you're doing.");
                add("secondary: " + fgm.getSecondaryFile());
                add("cleanBuild: " + fgm.getCleanBuild());
                add("batchFile: " + (batchFile == "" ? "blank" : (batchFile.startsWith(" ") ? batchFile.substring(1) : batchFile)));
            }
        };

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fgm.utils.getWorkingDirectory() + this.configFile, false));
            int index = 0;
            for (String line : configFile) {
                writer.write(line);
                if (++index != configFile.size())
                    writer.newLine();
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void defaultConfigs() {
        fgm.setSecondaryFile(false);
        fgm.setCleanBuild(false);
        fgm.utils.setBatchFile("");
    }
}
