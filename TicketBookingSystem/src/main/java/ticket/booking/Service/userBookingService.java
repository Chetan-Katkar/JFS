package ticket.booking.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Ticket;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class userBookingService {
    private User user;
    private boolean authenticated;
    private List<User> usersList;
    private static final String USER_PATH = "src/main/java/ticket/booking/LocalDB/Users.json";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final trainService trainsService;

    public userBookingService() throws IOException {
        File users = new File(USER_PATH);
        usersList = objectMapper.readValue(users, new TypeReference<List<User>>() {});
        trainsService = new trainService();
    }

    public userBookingService(User user) throws IOException {
        this();
        this.user = user;
    }

    public boolean loginUser() {
        authenticated = false;
        if (user == null || isBlank(user.getName()) || isBlank(user.getPassword())) {
            return false;
        }

        Optional<User> foundUser = usersList.stream()
                .filter(savedUser -> savedUser != null
                        && savedUser.getName() != null
                        && savedUser.getName().equalsIgnoreCase(user.getName()))
                .filter(savedUser -> UserServiceUtil.checkPassword(
                        user.getPassword(), savedUser.getHashPassword()))
                .findFirst();

        if (!foundUser.isPresent()) {
            return false;
        }

        // Use the persisted object so later booking changes are included when saving usersList.
        user = foundUser.get();
        authenticated = true;
        return true;
    }

    public boolean signUp(User newUser) throws IOException {
        if (newUser == null
                || isBlank(newUser.getName())
                || isBlank(newUser.getUserId())
                || isBlank(newUser.getPassword())) {
            return false;
        }

        String name = newUser.getName().trim();
        String userId = newUser.getUserId().trim();
        boolean alreadyExists = usersList.stream().anyMatch(savedUser -> savedUser != null
                && ((savedUser.getName() != null
                        && savedUser.getName().equalsIgnoreCase(name))
                    || (savedUser.getUserId() != null
                        && savedUser.getUserId().equals(userId))));
        if (alreadyExists) {
            return false;
        }

        List<Ticket> tickets = newUser.getTicketsBooked() == null
                ? new ArrayList<>()
                : new ArrayList<>(newUser.getTicketsBooked());
        User savedUser = new User(
                name,
                userId,
                null,
                UserServiceUtil.hashPassword(newUser.getPassword()),
                tickets);

        usersList.add(savedUser);
        try {
            saveUserListToFile();
        } catch (IOException e) {
            usersList.remove(savedUser);
            throw e;
        }

        // A successful signup starts a session using the object stored in usersList.
        user = savedUser;
        authenticated = true;
        return true;
    }

    private void saveUserListToFile() throws IOException {
        File usersFile = new File(USER_PATH);
        objectMapper.writeValue(usersFile, usersList);
    }

    public void fetchBookings() {
        if (!authenticated || user == null) {
            System.out.println("No user is currently logged in.");
            return;
        }
        user.printTicketsBooked();
    }

    public List<Train> getTrains(String source, String destination) {
        return trainsService.searchTrains(source, destination);
    }

    public List<List<Integer>> fetchSeats(Train train) {
        if (train == null) {
            return new ArrayList<>();
        }
        return trainsService.fetchSeats(train.getTrainId());
    }

    public boolean bookTrainSeat(Train train, String source, String destination,
                                 String dateOfTravel, int row, int column) throws IOException {
        if (!authenticated || user == null || train == null || isBlank(dateOfTravel)) {
            return false;
        }

        User savedUser = findSavedUser(user.getUserId());
        if (savedUser == null
                || !trainsService.servesRoute(train.getTrainId(), source, destination)
                || !trainsService.bookSeat(train.getTrainId(), row, column)) {
            return false;
        }

        if (savedUser.getTicketsBooked() == null) {
            savedUser.setTicketsBooked(new ArrayList<>());
        }

        Ticket ticket = new Ticket();
        ticket.setTicketId(UUID.randomUUID().toString());
        ticket.setUserId(savedUser.getUserId());
        ticket.setSource(source.trim());
        ticket.setDestination(destination.trim());
        ticket.setDateOfTravel(dateOfTravel.trim());
        ticket.setSeatRow(row);
        ticket.setSeatColumn(column);
        ticket.setTrain(trainsService.getTrainById(train.getTrainId()));
        savedUser.getTicketsBooked().add(ticket);

        try {
            saveUserListToFile();
        } catch (IOException e) {
            savedUser.getTicketsBooked().remove(ticket);
            try {
                trainsService.releaseSeat(train.getTrainId(), row, column);
            } catch (IOException rollbackException) {
                e.addSuppressed(rollbackException);
            }
            throw e;
        }

        user = savedUser;
        return true;
    }

    public boolean cancelBooking(String ticketId) throws IOException {
        if (!authenticated || user == null || isBlank(ticketId)) {
            return false;
        }

        User savedUser = findSavedUser(user.getUserId());
        if (savedUser == null || savedUser.getTicketsBooked() == null) {
            return false;
        }

        Ticket ticket = savedUser.getTicketsBooked().stream()
                .filter(bookedTicket -> bookedTicket != null
                        && ticketId.equals(bookedTicket.getTicketId()))
                .findFirst()
                .orElse(null);
        if (ticket == null) {
            return false;
        }

        boolean seatReleased = ticket.getTrain() != null
                && ticket.getSeatRow() != null
                && ticket.getSeatColumn() != null
                && trainsService.releaseSeat(
                        ticket.getTrain().getTrainId(),
                        ticket.getSeatRow(),
                        ticket.getSeatColumn());

        savedUser.getTicketsBooked().remove(ticket);

        try {
            saveUserListToFile();
        } catch (IOException e) {
            savedUser.getTicketsBooked().add(ticket);
            if (seatReleased) {
                try {
                    trainsService.bookSeat(
                            ticket.getTrain().getTrainId(),
                            ticket.getSeatRow(),
                            ticket.getSeatColumn());
                } catch (IOException rollbackException) {
                    e.addSuppressed(rollbackException);
                }
            }
            throw e;
        }

        user = savedUser;
        return true;
    }

    private User findSavedUser(String userId) {
        if (isBlank(userId)) {
            return null;
        }
        return usersList.stream()
                .filter(savedUser -> savedUser != null && userId.equals(savedUser.getUserId()))
                .findFirst()
                .orElse(null);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
