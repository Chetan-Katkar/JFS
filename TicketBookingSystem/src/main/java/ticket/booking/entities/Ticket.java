package ticket.booking.entities;

public class Ticket {
    private String ticketId;
    private String userId;
    private String source;
    private String destination;
    private String dateOfTravel;
    private Train train;
    private Integer seatRow;
    private Integer seatColumn;

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDateOfTravel() {
        return dateOfTravel;
    }

    public void setDateOfTravel(String dateOfTravel) {
        this.dateOfTravel = dateOfTravel;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public Integer getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(Integer seatRow) {
        this.seatRow = seatRow;
    }

    public Integer getSeatColumn() {
        return seatColumn;
    }

    public void setSeatColumn(Integer seatColumn) {
        this.seatColumn = seatColumn;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId='" + ticketId + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", dateOfTravel='" + dateOfTravel + '\'' +
                ", seatRow=" + (seatRow == null ? null : seatRow + 1) +
                ", seatColumn=" + (seatColumn == null ? null : seatColumn + 1) +
                '}';
    }
}
