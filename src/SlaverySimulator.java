// Завдання 1: Паралельне виконання асинхронних завдань
import java.util.concurrent.*;

public class SlaverySimulator {
    public static void main(String[] args) {

        // Визначаємо 3 задачі
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            int randomDelay = getRandomDelay();
            simulateLongRunningTask("Task1", randomDelay);
            return "Task 1 \n Dogs can't operate MRI machines. But catscan.";
        });

        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
            int randomDelay = getRandomDelay();
            simulateLongRunningTask("Task2", randomDelay);
            return "Task 2 \n Can a frog jump higher than a house? Of course, a house can't jump.";
        });

        CompletableFuture<String> task3 = CompletableFuture.supplyAsync(() -> {
            int randomDelay = getRandomDelay();
            simulateLongRunningTask("Task3", randomDelay);
            return "Task 3 \n I was going to try an all almond diet, but that's just nuts.";
        });

        // використовуємо anyOf для отримання результату - першої виконаної задачі
        CompletableFuture<Object> firstCompleted = CompletableFuture.anyOf(task1, task2, task3);

        // виводимо результат
        firstCompleted.thenAccept(result -> {
            System.out.println("\nFirst completed task: " + result);
        });

        // чекаємо виконання всіх задач
        sleep(10);
    }

    // допоміжний метод для симуляції задачі
    private static void simulateLongRunningTask(String taskName, int seconds) {
        System.out.println(taskName + " started (will take ~ " + seconds + " seconds)");
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\n" + taskName + " finished");
    }

    // метод для отримання випадкового числа від 1 до 10 для встановлення затримки у секундах
    private static int getRandomDelay() {
        return ThreadLocalRandom.current().nextInt(1, 10);
    }

    // допоміжний метод для підтримання життєдіяльності потоку до завершення виконання усіх задач
    private static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}