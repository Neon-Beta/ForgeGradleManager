package me.jezzadabomb.vp;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ForgeGradleManager {

    private JFrame jFrame;
    private JTextField versionField;
    private int numIndex = 0;
    private int stringLength = 0;

    JLabel batchFileName;
    JButton startBuild;
    private boolean trueDebug;
    private boolean debugMode;
    private boolean secondaryFile = false;
    private boolean cleanBuild = false;
    public String buildFile = "build.gradle";
    private ArrayList<String> oldFile = new ArrayList<String>();
    private ArrayList<Integer> intList = new ArrayList<Integer>();
    protected ConfigManager configManager;
    protected Utils utils;
    protected CommandParser parser;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ForgeGradleManager window = new ForgeGradleManager();
                    window.jFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public ForgeGradleManager() {
        configManager = new ConfigManager(this);
        utils = new Utils(this);
        parser = new CommandParser(this);

        debugMode = trueDebug = utils.isInDevEnvironment();

        if (!debugMode && !new File(utils.getGradlewBatch()).exists()) {
            utils.errorBox("Could not find gradlew.bat." + System.lineSeparator() + "Please place me in the correct folder.");
            System.exit(0);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                configManager.saveToConfig();
            }
        });

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        jFrame = new JFrame();
        jFrame.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent arg0) {
                startBuild.setText("Start build");
            }
        });
        jFrame.setTitle("Forge Gradle Manager " + utils.getFGMVersion());
        jFrame.setBounds(100, 100, 392, 199);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JButton btnSecondary = new JButton("Secondary");
        btnSecondary.setToolTipText("Select a batch file to run instead of gradlew.bat");

        startBuild = new JButton("Start build");
        startBuild.setBounds(70, 47, 186, 37);
        startBuild.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                startCommand();
            }
        });
        jFrame.getContentPane().setLayout(null);

        JButton incrementButton = new JButton("+");
        incrementButton.setToolTipText("Increment the version number.");
        incrementButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                setVersionNumber(getTextFieldAsNumber() + 1);
            }
        });
        incrementButton.setBounds(10, 9, 50, 37);
        jFrame.getContentPane().add(incrementButton);

        JButton decrementButton = new JButton("-");
        decrementButton.setToolTipText("Decrement the version number.");
        decrementButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                setVersionNumber(getTextFieldAsNumber() - 1);
            }
        });
        decrementButton.setBounds(10, 47, 50, 37);
        jFrame.getContentPane().add(decrementButton);

        versionField = new JTextField();
        versionField.setBounds(70, 17, 186, 20);
        versionField.setText(getVersionString());
        setVersionNumber(getTextFieldAsNumber() + 1);
        jFrame.getContentPane().add(versionField);
        versionField.setColumns(10);
        startBuild.setToolTipText("Change the version, and run the build script.");
        jFrame.getContentPane().add(startBuild);

        JCheckBox chckbxDebugMode = new JCheckBox("Debug");
        chckbxDebugMode.setToolTipText("Stuff that I'll probably remove.");
        chckbxDebugMode.setSelected(debugMode);
        if (!trueDebug) {
            chckbxDebugMode.setVisible(false);
            chckbxDebugMode.setEnabled(false);
        }
        chckbxDebugMode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                debugMode = !debugMode;
            }
        });
        chckbxDebugMode.setBounds(312, 129, 72, 23);
        jFrame.getContentPane().add(chckbxDebugMode);

        JButton installButton = new JButton("Install");
        installButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parser.parseGradleCommand("setupDevWorkspace");
            }
        });
        installButton.setBounds(266, 16, 100, 23);
        jFrame.getContentPane().add(installButton);

        JButton setupButton = new JButton("Setup");
        setupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parser.parseGradleCommand("eclipse");
            }
        });
        setupButton.setBounds(266, 38, 100, 23);
        jFrame.getContentPane().add(setupButton);

        JButton decompileButton = new JButton("Decompile");
        decompileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parser.parseGradleCommand("setupDecompWorkspace");
            }
        });
        decompileButton.setBounds(266, 60, 100, 23);
        jFrame.getContentPane().add(decompileButton);

        btnSecondary.setVisible(secondaryFile);
        btnSecondary.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                utils.setBatchFileFromSelector();
            }
        });

        JCheckBox chckbxCleanBuild = new JCheckBox("Clean build");
        chckbxCleanBuild.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                cleanBuild = !cleanBuild;
            }
        });
        chckbxCleanBuild.setBounds(249, 95, 97, 23);
        jFrame.getContentPane().add(chckbxCleanBuild);

        btnSecondary.setBounds(10, 129, 100, 23);
        jFrame.getContentPane().add(btnSecondary);

        JButton saveToConfig = new JButton("Save To Config");
        saveToConfig.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                configManager.saveToConfig();
            }
        });
        saveToConfig.setBounds(10, 95, 134, 23);
        jFrame.getContentPane().add(saveToConfig);

        batchFileName = new JLabel("File");
        batchFileName.setBounds(120, 133, 186, 14);
        jFrame.getContentPane().add(batchFileName);

        configManager.readFromConfig();

        JCheckBox chckbxSecondary = new JCheckBox("Secondary");
        chckbxSecondary.setToolTipText("Require a secondary build script?");
        chckbxSecondary.setSelected(secondaryFile);
        btnSecondary.setVisible(secondaryFile);
        batchFileName.setVisible(secondaryFile);
        chckbxCleanBuild.setSelected(cleanBuild);
        chckbxSecondary.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                secondaryFile = !secondaryFile;
                btnSecondary.setVisible(secondaryFile);
                batchFileName.setVisible(secondaryFile);
            }
        });
        chckbxSecondary.setBounds(150, 95, 97, 23);
        jFrame.getContentPane().add(chckbxSecondary);
    }

    private String getVersionString() {
        String version = "";
        boolean hitVersion = false;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(utils.getWorkingDirectory() + buildFile));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("version = ")) {
                    version = line.substring(11);
                    version = version.substring(0, version.length() - 1);
                    hitVersion = true;
                }
                if (!hitVersion)
                    numIndex++;
                oldFile.add(line);
            }
            reader.close();
        } catch (IOException e) {
        }
        if (version == "")
            version = "0.00.001";
        return version;
    }

    private void setVersionString() {
        String versionString = "version = \"" + versionField.getText() + "\"";
        int numIndex = 0;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(utils.getWorkingDirectory() + buildFile, false));
            boolean written = false;
            for (String line : oldFile) {
                if (numIndex == this.numIndex && !written) {
                    written = true;
                    writer.write(versionString + "\n");
                } else {
                    numIndex++;
                    writer.write(line + "\n");
                }
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long getTextFieldAsNumber() {
        String temp = versionField.getText();
        stringLength = temp.length();
        intList.clear();
        int index = 0;
        for (int i = 0; i < stringLength; i++) {
            if (temp.charAt(i) == '.') {
                intList.add(i);
                index++;
            }
        }
        temp = temp.replaceAll("[^\\d]", "");
        stringLength -= index;
        long num = 0;
        try {
            num = Long.parseLong(temp);
        } catch (NumberFormatException e) {
            System.out.println("Failed Parsing Num.");
            System.out.println("Number: " + temp);
        }
        return num;
    }

    private void setVersionNumber(long num) {
        if (num < 1)
            num = 1;

        String temp = String.valueOf(num);

        while (temp.length() < stringLength)
            temp = "0" + temp;
        for (Integer i : intList)
            temp = temp.substring(0, i) + "." + temp.substring(i);
        versionField.setText(temp);
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setCleanBuild(boolean cleanBuild) {
        this.cleanBuild = cleanBuild;
    }

    public boolean getCleanBuild() {
        return cleanBuild;
    }

    public boolean getSecondaryFile() {
        return secondaryFile;
    }

    public void setSecondaryFile(boolean secondaryFile) {
        this.secondaryFile = secondaryFile;
    }

    public void setBatchFileName(String text) {
        batchFileName.setText(text);
    }

    private void startCommand() {
        startBuild.setText("Building...");

        if (!debugMode)
            setVersionString();
        if (secondaryFile) {
            String command = utils.getSecondaryBatch();
            if (command == "") {
                utils.errorBox("You haven't selected a file for me to execute.");
                return;
            } else {
                parser.runCommand(command);
            }
        } else {
            if (cleanBuild) {
                parser.parseGradleCommand("clean build");
            } else {
                parser.parseGradleCommand("build");
            }
        }
        configManager.saveToConfig();
        setVersionNumber(getTextFieldAsNumber() + 1);
    }
}
