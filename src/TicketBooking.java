// Завдання 2: Бронювання, перевірка цін, і купівля авіаквитків
import java.util.concurrent.*;
import java.util.*;
import java.util.Comparator;

public class TicketBooking {

    // зберігаємо список місць та посилання на місце з найкращою ціною
    private List<Seat> seats = new ArrayList<>();
    private Seat bestSeat = null;

    public static void main(String[] args) {
        TicketBooking bookingExample = new TicketBooking();
        bookingExample.bookFlight();
    }

    public void bookFlight() {
        // перевірка наявності місць
        CompletableFuture<Boolean> seatAvailabilityFuture = checkSeatAvailability();

        // якщо є вільні місця, шукаємо найкращу ціну
        CompletableFuture<Double> bestPriceFuture = seatAvailabilityFuture
                .thenCompose(seatsAvailable -> {
                    if (seatsAvailable) {
                        System.out.println("Seats available. Checking best price...");
                        return findBestPrice();
                    } else {
                        return CompletableFuture.completedFuture(-1.0);
                    }
                });

        // поєднуємо доступність місця з найкращою ціною, завершуємо бронювання
        CompletableFuture<String> bookingResult = seatAvailabilityFuture
                .thenCombine(bestPriceFuture, (seatsAvailable, bestPrice) -> {
                    if (!seatsAvailable) {
                        return "Booking failed: No seats available.";
                    } else if (bestPrice <= 0) {
                        return "Booking failed: Unable to retrieve price.";
                    } else {
                        return payForTicket(bestPrice);
                    }
                });

        // чекаємо завершення виконання усіх завдань
        try {
            String result = bookingResult.get();
            System.out.println("Booking result: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    // симуляція наявності вільного місця (отримуємо true/false)
    private CompletableFuture<Boolean> checkSeatAvailability() {
        return CompletableFuture.supplyAsync(() -> {
            simulateDelay("Checking seat availability", 2);
            boolean seatsAvailable = Math.random() < 0.7;
            System.out.println("Seats available: " + seatsAvailable);

            // генеруємо кілька місць з випадковими параметрами ===
            seats = generateRandomSeats(5);

            // визначаємо, чи є принаймні одне доступне місце
            boolean actualSeatsAvailable = seats.stream().anyMatch(Seat::isAvailable);

            System.out.println("Generated seats:");
            seats.forEach(System.out::println);

            // повертаємо справжню наявність місць (на основі списку)
            return actualSeatsAvailable;
        });
    }

    // симуляція пошуку найкращої ціни (отримуємо випадкове значення)
    private CompletableFuture<Double> findBestPrice() {
        return CompletableFuture.supplyAsync(() -> {
            simulateDelay("Finding best price", 3);
            // обчислюємо мінімальну ціну з наявних місць
            bestSeat = seats.stream()
                    .filter(Seat::isAvailable)
                    .min(Comparator.comparingDouble(Seat::getPrice))
                    .orElse(null);

            if (bestSeat == null) {
                return -1.0;
            } else {
                System.out.println("Best price among seats: $"
                        + String.format("%.2f", bestSeat.getPrice()));
                return bestSeat.getPrice();
            }
        });
    }

    // процес оплати квитка
    private String payForTicket(double price) {
        simulateDelay("Processing payment of $" + String.format("%.2f", price), 2);
        if (bestSeat != null) {
            return "Payment successful. Ticket booked for seat "
                    + bestSeat.getName() + " at the price of: $"
                    + String.format("%.2f", bestSeat.getPrice());
        } else {
            return "Payment failed. No best seat found.";
        }
    }

    // допоміжний метод для симуляції асинхронної затримки
    private void simulateDelay(String action, int seconds) {
        System.out.println(action + " (approx. " + seconds + " seconds)...");
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // метод для генерації випадкових місць
    private List<Seat> generateRandomSeats(int count) {
        List<Seat> randomSeats = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String seatName = generateRandomSeatName();
            boolean isAvailable = (Math.random() < 0.6);
            double seatPrice = 50 + (300 * Math.random());
            randomSeats.add(new Seat(seatName, isAvailable, seatPrice));
        }
        return randomSeats;
    }

    // генеруємо випадкову назву місця
    private String generateRandomSeatName() {
        String prefix = "Seat-";
        String letters = getRandomLetters(2).toUpperCase();
        int digits = 1000 + (int)(Math.random() * 9000);
        return prefix + letters + digits;
    }

    // генеруємо випадкові літери
    private String getRandomLetters(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = (char)('a' + (int)(Math.random() * 26));
            sb.append(c);
        }
        return sb.toString();
    }

    // клас Seat для збереження інформації про місце
    private static class Seat {
        private String name;
        private boolean available;
        private double price;

        public Seat(String name, boolean available, double price) {
            this.name = name;
            this.available = available;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public boolean isAvailable() {
            return available;
        }

        public double getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return "Seat{name='" + name + '\'' +
                    ", available=" + available +
                    ", price=$" + String.format("%.2f", price) + '}';
        }
    }
}