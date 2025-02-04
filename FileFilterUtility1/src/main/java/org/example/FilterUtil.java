package org.example;

import org.example.datamanager.TextSorter;
import org.example.options.StartupOptions;

import java.io.FileWriter;
import java.io.IOException;

public class FilterUtil {
    //private static boolean appendMode = false;
    public static void main(String[] args) {

        final String help = String.format("java -jar FileFilterUtility.jar <args>%n" +
                "options:%n" +
                "-o <путь>     Путь до файлов с результатом.%n" +
                "-p <префикс>  Префикс имён файлов с результатом.%n" +
                "-a            Режим добавления в существующие файлы.%n" +
                "-s            Краткая статистика.%n" +
                "-f            Полная статистика.%n");

        if (args.length == 0 || args[0].equals("-h")) {
            System.out.print(help);
            return;
        }
        StartupOptions options = new StartupOptions(args);
        TextSorter sorter = new TextSorter(options);
        sorter.processFiles();
    }
}