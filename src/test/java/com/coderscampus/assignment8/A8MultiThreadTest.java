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
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(assignment::getNumbers, threadPool).thenAccept(numbers -> {
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
