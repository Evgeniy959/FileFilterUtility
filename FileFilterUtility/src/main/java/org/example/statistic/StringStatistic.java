package org.example.statistic;

public class StringStatistic extends Statistic {
    private int minLength = Integer.MAX_VALUE;
    private int maxLength = Integer.MIN_VALUE;

    @Override
    public void change(String line) {
        super.change(line);
        int length = line.length();
        if (length < minLength) minLength = length;
        if (length > maxLength) maxLength = length;
    }

    public String fullStatistics() {
        String baseSummary = shortStatistics();
        if (count > 0) {
            return baseSummary + ", минимальная длина строки: " + minLength + ", максимальная длина строки: " + maxLength;
        }
        return baseSummary;
    }
}
