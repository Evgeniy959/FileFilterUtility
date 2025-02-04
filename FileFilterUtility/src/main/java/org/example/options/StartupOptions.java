package org.example.options;

import java.util.ArrayList;
import java.util.List;

public class StartupOptions {
    private final String[] args;
    private static String outputPath = "./";
    private static String prefix = "";
    private static String inputFile;
    private static boolean appendMode = false;
    private boolean shortStat=false;
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

    public String getInputFile() {
        return inputFile;
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

    private void startup() {
        for (int i = 0; i < args.length; i++) {
            if ( args[i].equals("-o") && i+1<args.length) {
                outputPath = args[i+1];
            }
            if (args[i].equals("-a")) {
                appendMode = true;
            }
            if (args[i].equals("-p") && i+1<args.length){
                prefix = args[i+1];
            }
            if (args[i].endsWith(".txt")) {
                inputFiles.add(args[i]);
            }
            if(args[i].equals("-s")){
                shortStat = true;
            }
            if(args[i].equals("-f")){
                fullStat = true;
            }
            else{
                System.out.println("Введите команду. Для перехода к списку команд введите -h");
            }
        }

    }
}
