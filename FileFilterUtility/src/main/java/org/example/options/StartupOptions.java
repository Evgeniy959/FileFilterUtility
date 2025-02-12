package org.example.options;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StartupOptions {
    private final String[] args;
    private static String outputPath = "./";
    private static String prefix = "";
    private static boolean appendMode = false;
    private boolean shortStat = false;
    private boolean fullStat = false;
    private final List<String> inputFiles = new ArrayList<>();

    public StartupOptions(String[] args) {
        this.args = args;
        startup();
    }

    public String getOutputPath() {
        return outputPath;
    }

    public String getPrefix() {
        return prefix;
    }

    public List<String> getInputFiles() {
        return inputFiles;
    }

    public boolean isAppendMode() {
        return appendMode;
    }

    public boolean isShortStat() {
        return shortStat;
    }

    public boolean isFullStat() {
        return fullStat;
    }

    private void setOutputPath(int index) {
        if (index + 1 < args.length) {
            String checkPath = args[index + 1];
            try {
                Paths.get(checkPath);
                outputPath = checkPath;
            } catch (InvalidPathException e) {
                System.err.println("Указанный путь до файлов с результатом недопустим: " + checkPath);
            }
        } else {
            System.err.println("Не указан путь до файлов с результатом после опции -o");
        }
    }
    
    private void startup() {

        for (int i = 0; i < args.length; i++) {
            if ( args[i].equals("-o")) {
                setOutputPath(i);
            }
            if (args[i].equals("-a")) {
                appendMode = true;
            }
            if (args[i].equals("-p") && i+1<args.length){
                prefix = args[i+1];
            }
            if(args[i].equals("-s")){
                shortStat = true;
            }
            if(args[i].equals("-f")){
                fullStat = true;
            }
            if (args[i].endsWith(".txt")) {
                inputFiles.add(args[i]);
            }
        }
    }
}
