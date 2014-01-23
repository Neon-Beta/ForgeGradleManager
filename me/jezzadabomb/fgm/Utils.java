package me.jezzadabomb.fgm;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Utils {

    ForgeGradleManager fgm;
    public String gradleFile = "gradlew";
    public String batchFile = "";
    private String fgVersion = "v0.04";

    public Utils(ForgeGradleManager fgm) {
        this.fgm = fgm;
    }

    public boolean isInDevEnvironment() {
        return "true".equalsIgnoreCase(System.getProperty("jezza.debug"));
    }

    public String getFGMVersion() {
        return fgVersion;
    }

    public String getWorkingDirectory() {
        String path = ForgeGradleManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        return (path.substring(1, path.lastIndexOf('/') + 1)).replaceAll("%20", " ");
    }

    public String getGradlewBatch() {
        return getWorkingDirectory() + gradleFile;
    }

    public void errorBox(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Error!", JOptionPane.ERROR_MESSAGE);
    }

    public void setBatchFile(String batchFile) {
        this.batchFile = batchFile;
    }

    public String getBatchFile() {
        return batchFile;
    }

    public String getSecondaryBatch() {
        if (batchFile == "")
            return "";
        String temp = getWorkingDirectory() + batchFile;
        return temp;
    }

    public void setBatchFileFromSelector() {
        batchFile = getFileForExecute();
    }

    public String getFileForExecute() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Script files", "bat", "sh");
        chooser.setFileFilter(filter);

        int returnValue = chooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            String temp = chooser.getSelectedFile().getName();
            fgm.setBatchFileName(temp);
            return temp;
        }
        return "";
    }
}
