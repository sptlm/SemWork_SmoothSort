package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/*
ЗДЕСЬ ТОЛЬКО СОЗДАНИЕ ФАЙЛОВ С ВВОДНЫМИ ДАННЫМИ
ТЕСТЫ ИСКАТЬ В SmoothSort.main() и SmoothSort.test()
 */

/// Создание файлов с вводными данными
public class Main {
    public static void main(String[] args) {
        /// Случайные числа
        String fileName = "numbers.txt";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        Random rand = new Random();
        for(int i = 0; i<2800000;i+=1) {
            writer.write(Integer.toString(rand.nextInt(1,1_000_000_000)));
            writer.newLine();
        }
        } catch (IOException e) {
                throw new RuntimeException(e);
        }

        /// Отсортированные числа
        String fileNameSorted = "numbersSorted.txt";
        try {
            Random rand = new Random();

            BufferedWriter writer = new BufferedWriter(new FileWriter(fileNameSorted));

            for(int i = 0; i<28000000;i+=1) {
                int v = i;
                if (rand.nextFloat() < 0.02)
                    v+=3;
                writer.write(Integer.toString(v));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
