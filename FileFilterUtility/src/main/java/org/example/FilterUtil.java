package org.example;

import org.example.datamanager.TextSorter;
import org.example.options.Menu;
import org.example.options.StartupOptions;

public class FilterUtil {

    public static void main(String[] args) {
        if (args.length == 0 || args[0].equals("-h")) {
            Menu.listCommands(args);
        }
        else  {
            StartupOptions options = new StartupOptions(args);
            TextSorter sorter = new TextSorter(options);
            sorter.fileProcessing();
        }
    }
}