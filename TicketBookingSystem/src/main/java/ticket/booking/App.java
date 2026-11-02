package ticket.booking;

import ticket.booking.Service.userBookingService;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class App {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            userBookingService bookingService;
            try {
                bookingService = new userBookingService();
            } catch (IOException e) {
                System.out.println("Could not load the local booking data: " + e.getMessage());
                return;
            }

            boolean loggedIn = false;
            boolean running = true;
            List<Train> searchResults = new ArrayList<>();
            Train selectedTrain = null;
            String selectedSource = null;
            String selectedDestination = null;

            System.out.println("Running Train Booking System");
            while (running) {
                System.out.println("\nChoose option");
                System.out.println("1. Sign up");
                System.out.println("2. Login");
                System.out.println("3. Fetch bookings");
                System.out.println("4. Search trains");
                System.out.println("5. Book a seat");
                System.out.println("6. Cancel a booking");
                System.out.println("7. Exit the app");

                int option = readInt(scanner, "Option: ");
                switch (option) {
                    case 1: {
                        System.out.print("Enter a username: ");
                        String name = scanner.next();
                        System.out.print("Enter a password: ");
                        String password = scanner.next();

                        User newUser = new User(
                                name,
                                UUID.randomUUID().toString(),
                                password,
                                null,
                                new ArrayList<>());
                        try {
                            if (bookingService.signUp(newUser)) {
                                loggedIn = true;
                                selectedTrain = null;
                                System.out.println("Account created. You are now logged in.");
                            } else {
                                System.out.println("Signup failed. The username or user details may already exist.");
                            }
                        } catch (IOException e) {
                            System.out.println("Could not save the new account: " + e.getMessage());
                        }
                        break;
                    }
                    case 2: {
                        System.out.print("Enter your username: ");
                        String name = scanner.next();
                        System.out.print("Enter your password: ");
                        String password = scanner.next();

                        User loginDetails = new User(name, null, password, null, null);
                        try {
                            userBookingService loginService = new userBookingService(loginDetails);
                            if (loginService.loginUser()) {
                                bookingService = loginService;
                                loggedIn = true;
                                selectedTrain = null;
                                System.out.println("Login successful.");
                            } else {
                                loggedIn = false;
                                selectedTrain = null;
                                System.out.println("Username or password is incorrect.");
                            }
                        } catch (IOException e) {
                            System.out.println("Could not load the booking data: " + e.getMessage());
                        }
                        break;
                    }
                    case 3:
                        if (!loggedIn) {
                            System.out.println("Please log in before viewing bookings.");
                        } else {
                            bookingService.fetchBookings();
                        }
                        break;
                    case 4: {
                        System.out.print("Enter the source station: ");
                        selectedSource = scanner.next();
                        System.out.print("Enter the destination station: ");
                        selectedDestination = scanner.next();
                        searchResults = bookingService.getTrains(selectedSource, selectedDestination);

                        if (searchResults.isEmpty()) {
                            selectedTrain = null;
                            System.out.println("No trains with available seats were found for that route.");
                            break;
                        }

                        System.out.println("Available trains:");
                        for (int i = 0; i < searchResults.size(); i++) {
                            Train train = searchResults.get(i);
                            System.out.println((i + 1) + ". Train " + train.getTrainNo()
                                    + " (ID: " + train.getTrainId() + ")"
                                    + " | Station times: " + train.getStationTime());
                        }

                        int trainChoice = readInt(scanner, "Select a train number from the list: ");
                        if (trainChoice < 1 || trainChoice > searchResults.size()) {
                            selectedTrain = null;
                            System.out.println("That train selection is out of range.");
                        } else {
                            selectedTrain = searchResults.get(trainChoice - 1);
                            System.out.println("Selected train " + selectedTrain.getTrainNo() + ".");
                        }
                        break;
                    }
                    case 5: {
                        if (!loggedIn) {
                            System.out.println("Please log in before booking a seat.");
                            break;
                        }
                        if (selectedTrain == null) {
                            System.out.println("Search for trains and select one first.");
                            break;
                        }

                        List<List<Integer>> seats = bookingService.fetchSeats(selectedTrain);
                        if (seats.isEmpty()) {
                            System.out.println("No seat information is available for this train.");
                            break;
                        }

                        System.out.println("Seats (0 = available, 1 = booked):");
                        for (int row = 0; row < seats.size(); row++) {
                            System.out.print("Row " + (row + 1) + ": ");
                            for (Integer seat : seats.get(row)) {
                                System.out.print(seat + " ");
                            }
                            System.out.println();
                        }

                        System.out.print("Enter travel date (yyyy-MM-dd): ");
                        String dateOfTravel = scanner.next();
                        int row = readInt(scanner, "Enter the row number: ") - 1;
                        int column = readInt(scanner, "Enter the seat number in that row: ") - 1;

                        try {
                            boolean booked = bookingService.bookTrainSeat(
                                    selectedTrain,
                                    selectedSource,
                                    selectedDestination,
                                    dateOfTravel,
                                    row,
                                    column);
                            if (booked) {
                                System.out.println("Seat booked. Enjoy your journey!");
                                selectedTrain = null;
                            } else {
                                System.out.println("Could not book that seat. Check the route and seat selection.");
                            }
                        } catch (IOException e) {
                            System.out.println("Could not save the booking: " + e.getMessage());
                        }
                        break;
                    }
                    case 6: {
                        if (!loggedIn) {
                            System.out.println("Please log in before cancelling a booking.");
                            break;
                        }

                        System.out.print("Enter the ticket ID to cancel: ");
                        String ticketId = scanner.next();
                        try {
                            if (bookingService.cancelBooking(ticketId)) {
                                System.out.println("Booking cancelled and seat released.");
                            } else {
                                System.out.println("No booking was found with that ticket ID.");
                            }
                        } catch (IOException e) {
                            System.out.println("Could not save the cancellation: " + e.getMessage());
                        }
                        break;
                    }
                    case 7:
                        running = false;
                        break;
                    default:
                        System.out.println("Choose a number from 1 to 7.");
                }
            }
        }
    }

    private static int readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Enter a whole number. " + prompt);
            scanner.next();
        }
        return scanner.nextInt();
    }
}
