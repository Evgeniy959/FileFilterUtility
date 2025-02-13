package org.example.datamanager;

import org.example.options.StartupOptions;
import org.example.statistic.NumberStatistic;
import org.example.statistic.StringStatistic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TextSorter {

    private final StartupOptions options;
    private final ExecutorService executor;
    private final NumberStatistic integerStat = new NumberStatistic();
    private final NumberStatistic floatStat = new NumberStatistic();
    private final StringStatistic stringStat = new StringStatistic();

    public TextSorter(StartupOptions options) {
        this.options = options;
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void fileProcessing() {
        deleteOutputFiles();
        try {
            for (String inputFile : !options.getInputFiles().isEmpty() ? options.getInputFiles() : List.of("No file")) {
                executor.submit(() -> dataFilter(inputFile));
            }
            executor.shutdown();
            if (!executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
                executor.shutdownNow();
            }
            if (!options.getInputFiles().isEmpty()) {
                statisticsData();
            }
        } catch (InterruptedException e) {
            System.err.println("Выполнение прервано: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("Непредвиденная ошибка: " + e.getMessage());
        }
    }

    public synchronized List<String> loadContent(String name) {
        try {
            var config = new Properties();
            try (var is = ClassLoader.getSystemResourceAsStream("app.properties")) {
                config.load(is);
            }
            return Files.readAllLines(Paths.get(config.getProperty("file.path") + name));
        } catch (IOException e) {
            System.err.println("Неверно указан файл для чтения данных! Не удается найти файл: " + name + " " + e.getMessage());
            throw new RuntimeException();
        }
    }

    private void dataFilter(String input) {
        String type;
        List<String> lines = loadContent(input);
        for(String line : lines){
            if (DataParser.isInteger(line)) {
                type = TypeFile.INTEGER;
                integerStat.change(line);
            } else if (DataParser.isFloat(line)) {
                type = TypeFile.FLOAT;
                floatStat.change(line);
            } else {
                type = TypeFile.STRING;
                stringStat.change(line);
            }
            try {
                writeLine(type, line);
            } catch (IOException e) {
                System.err.println("Ошибка записи строк в файл: " + e.getMessage());
            }
        }
        System.out.println("Файл " + input + " успешно записан");
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

    private void deleteOutputFiles() {
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

    private void statisticsData() {
        if (options.isShortStat()) {
            System.out.println("Краткая статистика:");
            System.out.println("Integers: " + integerStat.shortStatistics());
            System.out.println("Floats: " + floatStat.shortStatistics());
            System.out.println("Strings: " + stringStat.shortStatistics());
        } else if (options.isFullStat()) {
            System.out.println("Полная статистика:");
            System.out.println("Integers: " + integerStat.fullStatistics());
            System.out.println("Floats: " + floatStat.fullStatistics());
            System.out.println("Strings: " + stringStat.fullStatistics());
        }
    }
}
