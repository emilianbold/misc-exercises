package com.example.exercises;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Given a limited set of math operations (e.g. 3x, +7, /5, -2), write a program
 * that lists the necessary math ops to produce the requested number (input).
 *
 * @see https://twitter.com/brunoborges/status/1234720194901053442
 */
public class Rec {

    static final String[] FUN_TEXT = new String[]{"* 3", "+ 7", "/ 5", "- 2"};
    static List<IntFunction<Integer>> FUN = Arrays.asList(
            x -> 3 * x,
            x -> x + 7,
            x -> {
                if (x % 5 == 0) {
                    return x / 5;
                } else {
                    //fallback; doesn't matter as it increases length
                    return Integer.MAX_VALUE;
                }
            },
            x -> x - 2);

    private static void solve(int n, List<Integer> ops) {
        while (true) {
            int x = compute(ops);

            if (x == n && !ops.isEmpty()) {
//                System.out.println("Found " + ops);
                String exp = ops.stream()
                        .map(i -> FUN_TEXT[i])
                        .collect(Collectors.joining(") "));

                String openPars = IntStream.range(0, ops.size() - 1)
                        .mapToObj(k -> "(")
                        .collect(Collectors.joining());

                exp = openPars + "0 " + exp;

                System.out.println("Found " + exp + " = " + n);
                break;
            }
            increment(ops);
        }
    }

    public static void main(String[] args) {
        solve(0, new ArrayList<>());
        solve(1, new ArrayList<>());
        solve(365, new ArrayList<>());
    }

    private static int compute(List<Integer> ops) {
        int start = 0;
        for (int x : ops) {
            start = FUN.get(x).apply(start);
        }
        return start;
    }

    private static void increment(List<Integer> ops) {
        int rem = 1;
        for (int i = ops.size() - 1; i >= 0; i--) {
            int v = ops.get(i) + rem;
            if (v >= FUN.size()) {
                rem = v / FUN.size();
                v = v % FUN.size();
            } else {
                rem = 0;
            }
            ops.set(i, v);
            if (rem == 0) {
                break;
            }
        }
        if (rem != 0) {
            ops.add(0, rem);
        }
    }

}
