package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class User {
    private String name;
    private String userId;
    private String password;
    private String hashPassword;
    private List<Ticket> ticketsBooked;

    public User() {}

    public User(String name, String userId, String password, String hashPassword,
                List<Ticket> ticketsBooked) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.hashPassword = hashPassword;
        this.ticketsBooked = ticketsBooked;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public List<Ticket> getTicketsBooked() {
        return ticketsBooked;
    }

    public void setTicketsBooked(List<Ticket> ticketsBooked) {
        this.ticketsBooked = ticketsBooked;
    }

    public void printTicketsBooked() {
        if (ticketsBooked == null || ticketsBooked.isEmpty()) {
            System.out.println("No tickets booked.");
            return;
        }

        System.out.println("Booked tickets:");
        for (int i = 0; i < ticketsBooked.size(); i++) {
            System.out.println((i + 1) + ". " + ticketsBooked.get(i));
        }
    }
}
