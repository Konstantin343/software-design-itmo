package ru.kkrukhmalev.app.arrays;

import java.util.Random;

public class ArraysCreator {
    public int[] createRandomArray(int n) {
        int[] array = new int[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt();
        }
        return array;
    }
}
