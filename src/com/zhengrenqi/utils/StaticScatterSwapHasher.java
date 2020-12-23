package com.zhengrenqi.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StaticScatterSwapHasher {

    public static long hash(long originalVal) {
        return hash(originalVal, 10);
    }

    public static long hash(long originalVal, int size) {
        String[] dd = String.format("%0" + size + "d", originalVal).split("");
        List<Integer> workingArray = Stream.of(dd).skip(Integer.max(dd.length - size, 0)).map(Integer::valueOf).collect(Collectors.toList());
        workingArray = swap(workingArray);
        workingArray = scatter(workingArray);
        return completedLong(workingArray);
    }

    public static long unhash(long originalVal) {
        return unhash(originalVal, 10);
    }

    public static long unhash(long originalVal, int size) {
        String[] dd = String.format("%0" + size + "d", originalVal).split("");
        List<Integer> workingArray = Stream.of(dd).skip(Integer.max(dd.length - size, 0)).map(Integer::valueOf).collect(Collectors.toList());
        workingArray = unscatter(workingArray);
        workingArray = unswap(workingArray);
        return completedLong(workingArray);
    }

    public static String hashString(long originalVal) {
        return hashString(originalVal, 10);
    }

    public static String hashString(long originalVal, int size) {
        String[] dd = String.format("%0" + size + "d", originalVal).split("");
        List<Integer> workingArray = Stream.of(dd).skip(Integer.max(dd.length - size, 0)).map(Integer::valueOf).collect(Collectors.toList());
        workingArray = swap(workingArray);
        workingArray = scatter(workingArray);
        return completedString(workingArray);
    }

    private static List<Integer> swap(List<Integer> workingArray) {
        List<Integer> swapList = new ArrayList<>();
        for (int i = 0; i < workingArray.size(); i++) {
            swapList.add(swapperList(i).get(workingArray.get(i)));
        }
        return swapList;
    }

    private static List<Integer> unswap(List<Integer> workingArray) {
        List<Integer> swapList = new ArrayList<>();
        for (int i = 0; i < workingArray.size(); i++) {
            swapList.add(swapperList(i).indexOf(workingArray.get(i)));
        }
        return swapList;
    }

    private static List<Integer> scatter(List<Integer> workingArray) {
        int sumOfDigits = 0;
        for (int i : workingArray) {
            sumOfDigits += i;
        }
        List<Integer> scatterList = new ArrayList<>();
        int size = workingArray.size();
        int spin = 0;
        for (int i = 0; i < size; i++) {
            Collections.rotate(workingArray, -(spin ^ sumOfDigits));
            scatterList.add(workingArray.remove(workingArray.size() - 1));
        }
        return scatterList;
    }

    private static List<Integer> unscatter(List<Integer> workingArray) {
        int sumOfDigits = workingArray.stream().reduce(Integer::sum).orElse((int) 0);
        List<Integer> unscatterList = new ArrayList<>();
        int size = workingArray.size();
        int spin = 0;
        for (int i = 0; i < size; i++) {
            unscatterList.add(workingArray.remove(workingArray.size() - 1));
            Collections.rotate(unscatterList, (sumOfDigits ^ spin));
        }
        return unscatterList;
    }

    public static int sum(int a, int b) {
        return (int) (a + b);
    }

    private static List<Integer> swapperList(int index) {
        int[][] arrs = {{9, 0, 2, 5, 1, 8, 4, 6, 3, 7},
                {0, 2, 5, 9, 7, 8, 4, 1, 3, 6},
                {1, 4, 8, 5, 3, 7, 6, 2, 0, 9},
                {2, 6, 1, 9, 0, 5, 7, 8, 3, 4},
                {3, 8, 5, 4, 7, 2, 9, 1, 0, 6},
                {4, 0, 8, 9, 3, 2, 7, 6, 1, 5},
                {5, 2, 1, 4, 9, 0, 8, 3, 7, 6},
                {6, 4, 5, 9, 7, 0, 1, 3, 8, 2},
                {7, 6, 9, 3, 2, 8, 1, 0, 5, 4},
                {8, 9, 2, 7, 0, 5, 3, 4, 6, 1}};
        if (index < 10) {
            return IntStream.of(arrs[index]).boxed().collect(Collectors.toList());
        } else {
            return swapperMap(index);
        }
    }

    private static List<Integer> swapperMap(int index) {

        List<Integer> array = new ArrayList<>();
        int radix = 10;
        for (int i = 0; i < radix; i++) {
            array.add(i);
        }
        List<Integer> arrayResult = new ArrayList<>();
        int spin = 0;
        for (int i = 0; i < radix; i++) {
            Collections.rotate(array, -index - (i ^ spin));
            array = new ArrayList<>(array);
            arrayResult.add(array.remove(array.size() - 1));
        }
        return arrayResult;
    }

    private static Long completedLong(List<Integer> workingArray) {
        return workingArray.stream().map(i -> (long) i).reduce((a, b) -> a * 10 + b).orElse(0L);
    }

    private static String completedString(List<Integer> workingArray) {
        StringBuilder buffer = new StringBuilder();
        for (int i : workingArray) {
            buffer.append(i);
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        long out = hash(10000L);
        System.out.println(out);
        long orig = unhash(out);
        System.out.println(orig);
    }

}

