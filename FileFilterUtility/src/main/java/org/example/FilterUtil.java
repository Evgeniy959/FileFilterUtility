package org.example;

import org.example.datamanager.TextSorter;
import org.example.options.StartupOptions;

public class FilterUtil {

    public static void main(String[] args) {
        StartupOptions options = new StartupOptions(args);
        TextSorter sorter = new TextSorter(options);
        sorter.fileProcessing();
    }
}