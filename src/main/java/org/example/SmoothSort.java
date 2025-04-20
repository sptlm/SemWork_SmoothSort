package org.example;

import java.io.*;
import java.util.*;

public class SmoothSort {

    // Счётчик итераций для оценки производительности
    private static long iterations = 0;

    // Основная функция сортировки Smoothsort
    public static void smoothsort(int[] lst) {
        // Генерируем список чисел Леонардо, подходящих по размеру
        List<Integer> leoNums = leonardoNumbers(lst.length);
        // Куча будет представлена как список размеров деревьев Леонардо
        List<Integer> heap = new ArrayList<>();

        // Строим кучу
        for (int i = 0; i < lst.length; i++) {
            iterations++;
            int heapSize = heap.size();
            // Проверка на возможность слияния двух последних деревьев
            if (heapSize >= 2 && heap.get(heapSize - 2) == heap.get(heapSize - 1) + 1) {
                heap.remove(heapSize - 1);
                heap.set(heapSize - 2, heap.get(heapSize - 2) + 1);
            } else {
                // Добавляем новое дерево
                if (heapSize >= 1 && heap.get(heapSize - 1) == 1) {
                    heap.add(0);
                } else {
                    heap.add(1);
                }
            }
            // Восстанавливаем свойства кучи
            restoreHeap(lst, i, heap, leoNums);
        }

        // Разбираем кучу обратно
        for (int i = lst.length - 1; i >= 0; i--) {
            iterations++;
            if (heap.get(heap.size() - 1) < 2) {
                heap.remove(heap.size() - 1);
            } else {
                // Делим дерево на два поддерева
                int k = heap.remove(heap.size() - 1);
                int[] childInfo = getChildTrees(i, k, leoNums);
                // t_r — индекс правого поддерева, k_r — его размер (в терминах индекса в числах Леонардо)
                int t_r = childInfo[0], k_r = childInfo[1];
                // t_l — индекс левого поддерева, k_l — его размер
                int t_l = childInfo[2], k_l = childInfo[3];

                heap.add(k_l);
                restoreHeap(lst, t_l, heap, leoNums);
                heap.add(k_r);
                restoreHeap(lst, t_r, heap, leoNums);
            }
        }
    }

    // Генерация чисел Леонардо, не превышающих размер массива
    private static List<Integer> leonardoNumbers(int hi) {
        List<Integer> numbers = new ArrayList<>();
        int a = 1, b = 1;
        while (a <= hi) {
            iterations++;
            numbers.add(a);
            int next = a + b + 1;
            a = b;
            b = next;
        }
        return numbers;
    }

    // Восстановление свойств кучи после вставки или разбора
    private static void restoreHeap(int[] lst, int i, List<Integer> heap, List<Integer> leoNums) {
        int current = heap.size() - 1;
        int k = heap.get(current);

        // Подъем вверх по дереву при необходимости
        while (current > 0) {
            iterations++;
            int j = i - leoNums.get(k);
            if (lst[j] > lst[i] &&
                    (k < 2 || (lst[j] > lst[i - 1] && lst[j] > lst[i - 2]))) {
                swap(lst, i, j);
                i = j;
                current--;
                k = heap.get(current);
            } else {
                break;
            }
        }

        // Просеивание вниз
        while (k >= 2) {
            iterations++;
            int[] childInfo = getChildTrees(i, k, leoNums);
            // t_r, k_r — правая вершина и её размер
            int t_r = childInfo[0], k_r = childInfo[1];
            // t_l, k_l — левая вершина и её размер
            int t_l = childInfo[2], k_l = childInfo[3];

            if (lst[i] < lst[t_r] || lst[i] < lst[t_l]) {
                if (lst[t_r] > lst[t_l]) {
                    swap(lst, i, t_r);
                    i = t_r;
                    k = k_r;
                } else {
                    swap(lst, i, t_l);
                    i = t_l;
                    k = k_l;
                }
            } else {
                break;
            }
        }
    }

    // Получение информации о поддеревьях для дерева Леонардо с индексом k
    private static int[] getChildTrees(int i, int k, List<Integer> leoNums) {
        iterations++;
        int t_r = i - 1;               // Индекс правого дочернего элемента
        int k_r = k - 2;               // Размер правого поддерева
        int t_l = t_r - leoNums.get(k_r); // Индекс левого дочернего элемента
        int k_l = k - 1;               // Размер левого поддерева
        return new int[]{t_r, k_r, t_l, k_l};
    }

    // Обмен значений в массиве
    private static void swap(int[] lst, int i, int j) {
        iterations++;
        int temp = lst[i];
        lst[i] = lst[j];
        lst[j] = temp;
    }

    public static void main(String[] args) {
        int[] arr = new int[]{6,3,7,8,6,4,3,55,26,52,48,25,1,36,84,58,2,5,0};

        System.out.println("Before: " + Arrays.toString(arr));
        long preTime1 = System.nanoTime();
        smoothsort(arr);
        long postTime1 = System.nanoTime();

        System.out.println("After: " + Arrays.toString(arr));
        System.out.println("Elapsed time: " + (postTime1 - preTime1));
        System.out.println("Iterations: " + iterations);

        test();
    }

    /// Тесты и запись в файл с результатами
    public static void test(){
        String fileName = "numbers.txt";

        /// шаг входных данных тестов
        int step = 20000;
        int roof = step * 50;
        long[] timers = new long[50];
        long[] iters = new long[50];
        int[] n = new int[50];

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            for(int i = step; i<=roof;i+=step) {
                iterations = 0;
                int[] array = new int[i];
                for (int j = 0; j < i; j++) {
                    array[j] = Integer.parseInt(reader.readLine());
                }
                long preTime = System.nanoTime();
                smoothsort(array);
                long postTime = System.nanoTime();

                System.out.println("Test № " + array.length / step);
                System.out.println("Elapsed time: " + (postTime - preTime));
                System.out.println("Iterations: " + iterations);

                timers[array.length/step-1] = postTime - preTime;
                iters[array.length/step-1] = iterations;
                n[array.length/step-1] = array.length;
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении из файла.");
            e.printStackTrace();

        }

        String resFileName = "results.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(resFileName))) {
            for (int i = 0; i < 50; i++) {
                writer.write("" + n[i] + " " + timers[i]+ " " + iters[i]);
                writer.newLine();
            }
            System.out.println("Массив записан в файл.");
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл.");
            e.printStackTrace();
        }
    }
}
