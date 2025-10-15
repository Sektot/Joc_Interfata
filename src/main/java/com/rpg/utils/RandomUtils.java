package com.rpg.utils;

import java.util.List;
import java.util.Random;

/**
 * Utilități pentru generare de valori random
 */
public class RandomUtils {

    private static final Random random = new Random();

    /**
     * Returnează true cu o anumită probabilitate (0-100%)
     */
    public static boolean chancePercent(double percent) {
        return random.nextDouble() * 100 < percent;
    }

    /**
     * Returnează un număr random între min și max (inclusiv)
     */
    public static int randomInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    /**
     * Returnează un element random dintr-o listă
     */
    public static <T> T randomElement(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(random.nextInt(list.size()));
    }

    /**
     * Returnează un element random dintr-un array
     */
    public static <T> T randomElement(T[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        return array[random.nextInt(array.length)];
    }

    /**
     * Returnează true/false random
     */
    public static boolean randomBoolean() {
        return random.nextBoolean();
    }

    /**
     * Returnează un double random între 0.0 și 1.0
     */
    public static double randomDouble() {
        return random.nextDouble();
    }

    /**
     * Aplică o variație aleatorie la o valoare.
     */
    public static int applyRandomVariation(int baseValue, int variationPercent) {
        if (variationPercent <= 0) return baseValue;

        double variation = (variationPercent / 100.0);
        double min = baseValue * (1.0 - variation);
        double max = baseValue * (1.0 + variation);

        return (int) randomDouble();
    }
}