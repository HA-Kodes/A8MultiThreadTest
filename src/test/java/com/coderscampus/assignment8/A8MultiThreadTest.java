package com.coderscampus.assignment8;

import com.coderscampus.assignment.Assignment8;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class A8MultiThreadTest {
    private ExecutorService threadPool;
        private Assignment8 assignment;

        @BeforeEach
        void setUp() {
            threadPool = Executors.newCachedThreadPool();
            assignment = new Assignment8();
        }

        @AfterEach
        void tearDown() {
            threadPool.shutdown();
        }

        @Test
        void fetchDataAsynchronously() {
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            Map<Integer, Integer> numberCounts = new ConcurrentHashMap<>();

            int ITERATION_COUNT = 1000;
            for (int i = 0; i < ITERATION_COUNT; i++) {
                CompletableFuture<Void> future = CompletableFuture.supplyAsync(assignment::getNumbers, threadPool)
                        .thenAccept(numbers -> {
                            numbers.forEach(num -> numberCounts.merge(num, 1, Integer::sum));
                        });
                futures.add(future);
            }

            futures.forEach(CompletableFuture::join);

            numberCounts.forEach((key, value) -> System.out.println(key + "=" + value));

            // Optional assertion to verify the total count
            int totalCount = numberCounts.values().stream().mapToInt(Integer::intValue).sum();
            assertEquals(ITERATION_COUNT * 1000, totalCount, "Total count of numbers does not match expected value.");
        }
    }


//        private final int OUTPUT_TXT_LINE_COUNT = 1000000;
//    private ExecutorService threadPool;
//
//        // Simulate loading numbers from a file
//        public List<Integer> getNumbers() {
//            List<Integer> numbers = new ArrayList<>();
//            Random random = new Random();
//
//            // Generate 100 random integers within the valid range
//            int numberOfIntegers = 100;
//            for (int i = 0; i < numberOfIntegers; i++) {
//                int num = random.nextInt(OUTPUT_TXT_LINE_COUNT);  // Generate a number within the valid range
//                numbers.add(num);
//            }
//
//            return numbers;
//        }
//
//        @BeforeEach
//        void setUp() {
//            threadPool = Executors.newCachedThreadPool();
//        }
//
//        @AfterEach
//        void tearDown() {
//            threadPool.shutdown();
//        }
//
//        @Test
//        void fetchDataAsynchronously() {
//            List<CompletableFuture<Void>> futures = new ArrayList<>();
//            Map<Integer, Integer> numberCounts = new ConcurrentHashMap<>();
//            int iterationCount = getITERATION_COUNT(); // Use method to get ITERATION_COUNT
//
//            for (int i = 0; i < iterationCount; i++) {
//                CompletableFuture<Void> future = CompletableFuture.supplyAsync(this::getNumbers, threadPool)
//                        .thenAccept(numbers -> numbers.forEach(num -> numberCounts.merge(num, 1, Integer::sum)));
//                futures.add(future);
//            }
//
//            futures.forEach(CompletableFuture::join);
//
//            numberCounts.forEach((key, value) -> System.out.println(key + "=" + value));
//        }
//
//        @Test
//        void fetchDataWithExperimentRunCount() {
//            List<CompletableFuture<Void>> futures = new ArrayList<>();
//            Map<Integer, Integer> numberCounts = new ConcurrentHashMap<>();
//
//            int EXPERIMENT_RUN_COUNT = 100;
//            for (int i = 0; i < EXPERIMENT_RUN_COUNT; i++) {
//                CompletableFuture<Void> future = CompletableFuture.supplyAsync(this::getNumbers, threadPool)
//                        .thenAccept(numbers -> numbers.forEach(num -> numberCounts.merge(num, 1, Integer::sum)));
//                futures.add(future);
//            }
//
//            futures.forEach(CompletableFuture::join);
//
//            numberCounts.forEach((key, value) -> System.out.println(key + "=" + value));
//        }
//
//        @Test
//        void validateGetNumbers() {
//            List<Integer> numbers = getNumbers();
//            numbers.forEach(num -> assertTrue(num >= 0 && num < OUTPUT_TXT_LINE_COUNT, "Index " + num + " out of bounds for length " + OUTPUT_TXT_LINE_COUNT));
//        }
//
//        // Use this method to return ITERATION_COUNT if needed
//        public int getITERATION_COUNT() {
//            return 1000;
//        }
//    }
