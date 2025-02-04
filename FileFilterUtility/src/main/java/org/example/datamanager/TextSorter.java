package org.example.datamanager;

import org.example.options.StartupOptions;
import org.example.options.TypeFile;
import org.example.statistic.NumberStatistic;
import org.example.statistic.StringStatistic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
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
    private final NumberStatistic integerStat = new NumberStatistic();
    private final NumberStatistic floatStat = new NumberStatistic();
    private final StringStatistic stringStat = new StringStatistic();
    private final  List<BigInteger> integers = new ArrayList<>();
    private final List<String> strings = new ArrayList<>();
    private final  List<BigDecimal> floats = new ArrayList<>();

    public TextSorter(StartupOptions options) {
        this.options = options;
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void processFiles() {
        clearOutputFilesIfRequired();
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
            summarizeStatistics();
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

    public synchronized List<String> loadContent(String name) {
        try {
//            var is = ClassLoader.getSystemResource("input/" + name);
//            System.out.println(is);
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
//        for (int i = 0; i < lines.size(); i++) {
//            System.out.println(lines.get(i));
//        }
        //String[] lines = str.split("/n");

        for(String line : lines){
            if (DataParser.isInteger(line)) {
                type = TypeFile.INTEGER;
                //integers.add()
                integerStat.update(line);
            } else if (DataParser.isFloat(line)) {
                type = TypeFile.FLOAT;
                floatStat.update(line);
            } else {
                type = TypeFile.STRING;
                stringStat.update(line);
            }
            try {
                writeLine(type, line);
            } catch (IOException e) {
                System.err.println("Не удалось записать строку в файл: " + e.getMessage());
            }
        }
    }

    private void writeLine(String type, String line) throws IOException {
        String fileName = options.getOutputPath() + "/" + options.getPrefix() + type + ".txt";
        Path filePath = Paths.get(fileName);
        Path parentDir = filePath.getParent();
        if (!Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
                writer.write(line);
                writer.newLine();
        }
    }

    private void clearOutputFilesIfRequired() {
        if (!options.isAppendMode()) {
            String[] fileTypes = {TypeFile.INTEGER, TypeFile.FLOAT, TypeFile.STRING};
            for (String type : fileTypes) {
                String fileName = options.getOutputPath() + "/" + options.getPrefix() + type + ".txt";
                Path filePath = Paths.get(fileName);
                try {
                    Files.deleteIfExists(filePath);
                } catch (IOException e) {
                    System.err.println("Не удалось удалить существующий файл: " + fileName + ". " + e.getMessage());;
                }
            }
        }
    }

    private void summarizeStatistics() {
        if (options.isShortStat()) {
            System.out.println("Short statistics:");
            System.out.println("Integers: " + integerStat.shortSummarize());
            System.out.println("Floats: " + floatStat.shortSummarize());
            System.out.println("Strings: " + stringStat.shortSummarize());
        } else if (options.isFullStat()) {
            System.out.println("Full statistics:");
            System.out.println("Integers: " + integerStat.summarize());
            System.out.println("Floats: " + floatStat.summarize());
            System.out.println("Strings: " + stringStat.summarize());
        }
    }
}
