package org.example.statistic;

public class Statistic {
    protected int count;

    public void change(String line) {
        count++;
    }

    public String shortStatistics() {
        return "количество элементов: " + count;
    }
}
