package org.example.datamanager;

import org.example.options.StartupOptions;
import org.example.options.TypeFile;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TextSorter {

    private final StartupOptions options;
    private final ExecutorService executor;
    StringBuffer bufferIntegers = new StringBuffer();
    StringBuffer bufferFloats = new StringBuffer();
    StringBuffer bufferStrings = new StringBuffer();
    List<String> lines = new ArrayList<>();
    boolean flag = false;

    public TextSorter(StartupOptions options) {
        this.options = options;
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void processFiles() {
        try {
            for (String inputFile : options.getInputFiles()) {
                executor.submit(() -> {
                    dataFilter(inputFile);
                });
            }
            executor.shutdown();
            if (!executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
                executor.shutdownNow();
            }
            //summarizeStatistics();
        } catch (InterruptedException e) {
            System.err.println("Выполнение прервано: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("Непредвиденная ошибка: " + e.getMessage());
        }
//        for (String inputFile : options.getInputFiles()) {
//            //executor.submit(() -> {
//            dataFilter(inputFile);
//        }
        //dataFilter(options.getInputFile());

    }
//public void processFiles() {
//
//    try {
//        for (String inputFile : options.getInputFiles()) {
//            if (inputFile.equals("in1.txt")){
//                if (!flag){
//                    dataFilter(inputFile);
//                }
//
//            } else /*if (flag)*/{
//                //inputFile = "in2.txt";
//                dataFilter(inputFile);
//                flag = true;
//            }
//        }
//    } catch (Exception e) {
//        System.err.println("Непредвиденная ошибка: " + e.getMessage());
//    }
//}

    private synchronized List<String> loadContent(String name) {
        try {
//            var is = ClassLoader.getSystemResource("input/" + name);
//            System.out.println(is);

            return Files.readAllLines(Paths.get("D:\\D Aser\\Test2\\Java\\JavaProjects\\Projects\\FileFilterUtility\\src\\main\\java\\org\\example\\input\\" + name), UTF_8);
            //return lines;
//            var is = ClassLoader.getSystemResourceAsStream("input/" + name + ".txt");
//            return new String(is.readAllBytes());
        } catch (IOException e) {
            System.err.println("Неправильно указан файл для чтения данных: " + name + e.getMessage());
            throw new RuntimeException("Не удается найти файл!");
        }
        //return List.of("apple", "banana", "orange");
    }

    private void dataFilter(String input) {
        String type;
        //List<String> lines = loadContent(input);
//        for (int i = 0; i < loadContent(input).size(); i++) {
//            System.out.println(lines.get(i));
//        }
        //String[] lines = str.split("/n");

        for(String line : loadContent(input)){
            if (DataParser.isInteger(line)) {
                //type = "integers";
                bufferIntegers.append(String.format(line+"%n"));
                //writeLine(type, bufferIntegers.toString());
                //for (int i = 0; i < lines.length; i++) {
                    //System.out.println(bufferIntegers.toString());
                //}
                //integerStat.update(line);
            } else if (DataParser.isFloat(line)) {
                //type = "floats";
                bufferFloats.append(String.format(line+"%n"));
                //writeLine(type, bufferFloats.toString());
                //floatStat.update(line);
            } else {
                //type = "strings";
                bufferStrings.append(String.format(line+"%n"));
                //writeLine(type, bufferStrings.toString());
                //stringStat.update(line);
            }
            //if (!flag) break;
        }
        //System.out.println(bufferFloats.toString());
        try {
            writeLine();
        } catch (IOException e) {
            System.err.println("Не удалось записать строку в файл: " + e.getMessage());
        }
//        if (Parser.isInteger(line)) {
//            type = Constants.INTEGER;
//            integerStat.update(line);
//        } else if (Parser.isFloat(line)) {
//            type = Constants.FLOAT;
//            floatStat.update(line);
//        } else {
//            type = Constants.STRING;
//            stringStat.update(line);
//        }

//        try {
//            writeLine(type, line);
//        } catch (IOException e) {
//            System.err.println("Failed to write line to file: " + e.getMessage());
//        }
    }

    private void writeLine() throws IOException {
        //String fileName = options.getOutputPath() + "/" + options.getPrefix() + type + ".txt";
        System.out.println(bufferFloats.toString());
        String[] fileTypes = {TypeFile.INTEGER, TypeFile.FLOAT, TypeFile.STRING};
        for (String type : fileTypes) {
            String fileName = options.getOutputPath() + "/" + options.getPrefix() + type + ".txt";
            try (FileWriter writer = new FileWriter(fileName, options.isAppendMode())) {
                if (type.equals(TypeFile.INTEGER)){
                    writer.write(bufferIntegers.toString());
                    writer.flush();
                    //writer.newLine();
                }
                else if (type.equals(TypeFile.FLOAT)){
                    writer.write(bufferFloats.toString());
                    writer.flush();
                    //writer.newLine();
                }
                else if (type.equals(TypeFile.STRING)){
                    writer.write(bufferStrings.toString());
                    writer.flush();
                    //writer.newLine();
                }

            }
        }
//        Path filePath = Paths.get(fileName);
//        Path parentDir = filePath.getParent();
//
//        if (!Files.exists(parentDir)) {
//            Files.createDirectories(parentDir);
//        }
    }
}
