package Task2;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

/**
 * Примерные результаты по прохождению бенчмарка на текущих настройках
 *
 * Запускаем Тест 1 synchronizedMap (10 сек)
 * время выполнения 18
 *  максимальное время выполнения записи 11
 *  максималное время выполнения чтения 12
 * Запускаем Тест 2 ConcurrentHashMap (10 сек)
 * время выполнения 8
 *  максимальное время выполнения записи 7
 *  максималное время выполнения чтения 2
 *
 *  Незначительная разница скорость при массивах до 1000 элементов.
 *  synchronizedMap отличается тем, что скорость записи и чтения примерно одинаковая всегда,
 *      когда в ConcurrentHashMap скорость записи ниже, чем скорость чтения в разы.
 */


public class Main {

    private static Random random = new Random();

    public static void main(String[] args) throws InterruptedException {

        int procCount = Runtime.getRuntime().availableProcessors();


        Map<Integer, String> test1 = Collections.synchronizedMap(new HashMap<>());
        ConcurrentHashMap<Integer, String> test2 = new ConcurrentHashMap();

        final int ThreadNumWrite = 5,
                ThreadNumRead = 5,
                listSize = 10000,
                awaitTermination_Test1 = 10,
                awaitTermination_Test2 = 10;

        final List<Integer> testList = getArrForTest(listSize);

        AtomicLong maxExecTimeWrite = new AtomicLong(1);
        AtomicLong maxExecTimeRead = new AtomicLong(1);
        AtomicLong lastExecTime = new AtomicLong(1);
        AtomicLong sum = new AtomicLong(0);

        int batchSize = (listSize/ThreadNumWrite);

        startTest1(
                procCount,
                test1,
                ThreadNumWrite,
                ThreadNumRead,
                awaitTermination_Test1,
                testList,
                maxExecTimeWrite,
                maxExecTimeRead,
                lastExecTime,
                sum,
                batchSize
        );

        startTest2(
                procCount,
                test2,
                ThreadNumWrite,
                ThreadNumRead,
                awaitTermination_Test2,
                testList,
                maxExecTimeWrite,
                maxExecTimeRead,
                lastExecTime,
                sum,
                batchSize
        );
    }

    private static void startTest1(int procCount, Map<Integer, String> test1, int threadNumWrite, int threadNumRead, int awaitTermination_Test1, List<Integer> testList, AtomicLong maxExecTimeWrite, AtomicLong maxExecTimeRead, AtomicLong lastExecTime, AtomicLong sum, int batchSize) throws InterruptedException {

        long execTime;
        long startExecTime;
        ExecutorService executorService = Executors.newFixedThreadPool(procCount);

        lastExecTime.set(0);
        maxExecTimeWrite.set(0);
        maxExecTimeRead.set(0);
        sum.set(0);

        System.out.printf("Запускаем Тест 1 synchronizedMap (%d сек)\n", awaitTermination_Test1);
        startExecTime = System.currentTimeMillis();

        IntStream.range(1, threadNumWrite + 1).forEach(i -> {
            executorService.submit(() -> {
                long st = System.currentTimeMillis();
                testList.subList(batchSize * (i -1), batchSize * i).forEach(e ->{
                    test1.put(e, String.valueOf(e));
                });
                long end = System.currentTimeMillis();
                lastExecTime.set(end);
                if (maxExecTimeWrite.get() < end - st) maxExecTimeWrite.set(end - st);
            });
        });
        IntStream.range(1, threadNumRead + 1).forEach(i -> {
            executorService.submit(() -> {
                long st = System.currentTimeMillis();
                testList.subList(batchSize * (i -1), batchSize * i).forEach(e ->{
                    sum.getAndAdd(Long.parseLong(test1.get(e)));
                });
                long end = System.currentTimeMillis();
                lastExecTime.set(end);
                if (maxExecTimeRead.get() < end - st) maxExecTimeRead.set(end - st);
            });
        });

        execTime = lastExecTime.get() - startExecTime;

        executorService.awaitTermination(awaitTermination_Test1, TimeUnit.SECONDS);

        System.out.printf("время выполнения %d \n максимальное время выполнения записи %d \n" +
                " максималное время выполнения чтения %d \n", execTime, maxExecTimeWrite.get(), maxExecTimeRead.get());

        executorService.shutdown();
    }

    private static void startTest2(int procCount, ConcurrentHashMap<Integer, String> test2,
                                   int threadNumWrite, int threadNumRead, int awaitTermination_Test2,
                                   List<Integer> testList, AtomicLong maxExecTimeWrite, AtomicLong maxExecTimeRead,
                                   AtomicLong lastExecTime, AtomicLong sum, int batchSize)
            throws InterruptedException {

        long execTime;
        long startExecTime;
        ExecutorService executorService2 = Executors.newFixedThreadPool(procCount);

        lastExecTime.set(0);
        maxExecTimeWrite.set(0);
        maxExecTimeRead.set(0);
        sum.set(0);

        System.out.printf("Запускаем Тест 2 ConcurrentHashMap (%d сек)\n", awaitTermination_Test2);
        startExecTime = System.currentTimeMillis();

        IntStream.range(1, threadNumWrite + 1).forEach(i -> {
            executorService2.submit(() -> {
                long st = System.currentTimeMillis();
                testList.subList(batchSize * (i -1), batchSize * i).forEach(e ->{
                    test2.put(e, String.valueOf(e));
                });
                long end = System.currentTimeMillis();
                lastExecTime.set(end);
                if (maxExecTimeWrite.get() < end - st) maxExecTimeWrite.set(end - st);
            });
        });
        IntStream.range(1, threadNumRead + 1).forEach(i -> {
            executorService2.submit(() -> {
                long st = System.currentTimeMillis();
                testList.subList(batchSize * (i -1), batchSize * i).forEach(e ->{
                    sum.getAndAdd(Long.parseLong(test2.get(e)));
                });
                long end = System.currentTimeMillis();
                lastExecTime.set(end);
                if (maxExecTimeRead.get() < end - st) maxExecTimeRead.set(end - st);
            });
        });

        execTime = lastExecTime.get() - startExecTime;

        executorService2.awaitTermination(awaitTermination_Test2, TimeUnit.SECONDS);

        System.out.printf("время выполнения %d \n максимальное время выполнения записи %d \n" +
                " максималное время выполнения чтения %d \n", execTime, maxExecTimeWrite.get(), maxExecTimeRead.get());

        executorService2.shutdown();
    }

    static List<Integer> getArrForTest(int size) {
        List<Integer> res = new ArrayList<>();
        IntStream.range(0, size).forEach(i -> res.add(rand(0, 1000)));
        return res;
    }

    public static int rand(int min, int max) {
        return random.nextInt(max + 1 - min) + min;
    }
}
