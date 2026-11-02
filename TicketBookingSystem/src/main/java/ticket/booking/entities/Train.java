package ticket.booking.entities;

import java.util.List;
import java.util.Map;

public class Train {
    private String trainId;
    private String trainNo;
    private List<List<Integer>> seats;
    private Map<String, String> stationTime;
    private List<String> stations;

    public Train() {}

    public Train(String trainId, String trainNo, List<List<Integer>> seats,
                 Map<String, String> stationTime, List<String> stations) {
        this.trainId = trainId;
        this.trainNo = trainNo;
        this.seats = seats;
        this.stationTime = stationTime;
        this.stations = stations;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public List<List<Integer>> getSeats() {
        return seats;
    }

    public void setSeats(List<List<Integer>> seats) {
        this.seats = seats;
    }

    public Map<String, String> getStationTime() {
        return stationTime;
    }

    public void setStationTime(Map<String, String> stationTime) {
        this.stationTime = stationTime;
    }

    public List<String> getStations() {
        return stations;
    }

    public void setStations(List<String> stations) {
        this.stations = stations;
    }

    public void printTrainDetails() {
        System.out.println("Train ID: " + trainId);
        System.out.println("Train number: " + trainNo);
        System.out.println("Seats:");
        if (seats == null || seats.isEmpty()) {
            System.out.println("  No seat data.");
        } else {
            for (List<Integer> row : seats) {
                System.out.println("  " + row);
            }
        }
        System.out.println("Stations: " + stations);
        System.out.println("Station times: " + stationTime);
    }
}
