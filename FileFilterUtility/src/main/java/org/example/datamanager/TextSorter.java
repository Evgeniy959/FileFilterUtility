package org.example.datamanager;

import org.example.options.StartupOptions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TextSorter {

    private final StartupOptions options;
    private final ExecutorService executor;
    StringBuilder builderLines = new StringBuilder();

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

    public List<String> loadContent(String name) {
        try {
            return Files.readAllLines(Paths.get("D:\\D Aser\\Test2\\Java\\JavaProjects\\Projects\\FileFilterUtility\\src\\main\\java\\org\\example\\input\\" + name), UTF_8);
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
        List<String> lines = loadContent(input);
        for (int i = 0; i < lines.size(); i++) {
            System.out.println(lines.get(i));
        }
        //String[] lines = str.split("/n");

        for(String line : lines){
            if (DataParser.isInteger(line)) {
                type = "integers";
                //builderLines.append(String.format(line+"%n"));
                //for (int i = 0; i < lines.length; i++) {
                    System.out.println(lines.get(1));
                //}
                //integerStat.update(line);
            } else if (DataParser.isFloat(line)) {
                type = "floats";
                //builderLines.append(String.format(line+"%n"));
                //floatStat.update(line);
            } else {
                type = "strings";
                //builderLines.append(String.format(line+"%n"));
                //stringStat.update(line);
            }
            try {
                writeLine(type, line);
            } catch (IOException e) {
                System.err.println("Не удалось записать строку в файл: " + e.getMessage());
            }
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

    private synchronized void writeLine(String type, String line) throws IOException {
        String fileName = options.getOutputPath() + "/" + options.getPrefix() + type + ".txt";
//
//        Path filePath = Paths.get(fileName);
//        Path parentDir = filePath.getParent();
//
//        if (!Files.exists(parentDir)) {
//            Files.createDirectories(parentDir);
//        }
//
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, options.isAppendMode()))) {
            writer.write(line);
            writer.newLine();
        }
    }
}
