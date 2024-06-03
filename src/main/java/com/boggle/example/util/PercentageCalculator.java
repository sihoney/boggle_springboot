package com.boggle.example.util;

import java.text.DecimalFormat;

public class PercentageCalculator {

    public static double calculatePercentage(double obtained, double total) {
        if (total == 0) {
            throw new IllegalArgumentException("Total value cannot be zero");
        }
        return (obtained / total) * 100;
    }

    public static void main(String[] args) {
        double obtained = 75;
        double total = 150;

        double percentage = calculatePercentage(obtained, total);

        // DecimalFormat을 사용하여 퍼센티지 소수점 두 자리로 포맷팅
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("Percentage: " + df.format(percentage) + "%");
    }
}
