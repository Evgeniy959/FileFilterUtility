package org.example.options;

public class Menu {
    public static void listCommands(String[] args) {
        final String help = String.format("java -jar FileFilterUtility.jar <args>%n" +
                "options:%n" +
                "-o <путь>     Путь до файлов с результатом.%n" +
                "-p <префикс>  Префикс имён файлов с результатом.%n" +
                "-a            Режим добавления в существующие файлы.%n" +
                "-s            Краткая статистика.%n" +
                "-f            Полная статистика.%n");

        if (args.length == 0) {
            System.out.print("Введите команду. Для перехода к списку команд введите -h");
            return;
        }
        if (args[0].equals("-h")) {
            System.out.print(help);
            return;
        }
    }
}
