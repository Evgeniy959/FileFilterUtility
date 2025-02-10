package org.example.statistic;

public class NumberStatistic extends Statistic {
    private double min = Double.MAX_VALUE;
    private double max = Double.MIN_VALUE;
    private double sum = 0;

    @Override
    public void change(String line) {
        super.change(line);
        double value = Double.parseDouble(line);
        if (value < min) min = value;
        if (value > max) max = value;
        sum += value;
    }

    public String fullStatistics() {
        String baseSummary = shortStatistics();
        if (count > 0) {
            double average = sum / count;
            return baseSummary + ", минимальное значение: " + min + ", максимальное значение: " + max + ", сумма: " + sum + ", среднее: " + average;
        }
        return baseSummary;
    }
}
