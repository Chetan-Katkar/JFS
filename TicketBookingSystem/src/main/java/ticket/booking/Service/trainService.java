package ticket.booking.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class trainService {
    private static final String TRAIN_PATH = "src/main/java/ticket/booking/LocalDB/Trains.json";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Train> trains;

    public trainService() throws IOException {
        File trainFile = new File(TRAIN_PATH);
        if (trainFile.exists()) {
            List<Train> loadedTrains = objectMapper.readValue(
                    trainFile, new TypeReference<List<Train>>() {});
            trains = loadedTrains == null ? new ArrayList<>() : loadedTrains;
        } else {
            trains = new ArrayList<>();
        }
    }

    public List<Train> searchTrains(String source, String destination) {
        List<Train> matches = new ArrayList<>();
        if (isBlank(source) || isBlank(destination)) {
            return matches;
        }

        for (Train train : trains) {
            if (train == null || !hasAvailableSeat(train)) {
                continue;
            }

            int sourceIndex = indexOfStation(train, source);
            int destinationIndex = indexOfStation(train, destination);
            if (sourceIndex >= 0 && destinationIndex > sourceIndex) {
                matches.add(train);
            }
        }
        return matches;
    }

    public Train getTrainById(String trainId) {
        if (isBlank(trainId)) {
            return null;
        }
        for (Train train : trains) {
            if (train != null && trainId.equals(train.getTrainId())) {
                return train;
            }
        }
        return null;
    }

    public List<List<Integer>> fetchSeats(String trainId) {
        Train train = getTrainById(trainId);
        if (train == null || train.getSeats() == null) {
            return new ArrayList<>();
        }

        List<List<Integer>> seatCopy = new ArrayList<>();
        for (List<Integer> row : train.getSeats()) {
            seatCopy.add(row == null ? new ArrayList<>() : new ArrayList<>(row));
        }
        return seatCopy;
    }

    public boolean bookSeat(String trainId, int row, int column) throws IOException {
        Train train = getTrainById(trainId);
        if (!isAvailableSeat(train, row, column)) {
            return false;
        }

        train.getSeats().get(row).set(column, 1);
        try {
            saveTrains();
        } catch (IOException e) {
            train.getSeats().get(row).set(column, 0);
            throw e;
        }
        return true;
    }

    public boolean releaseSeat(String trainId, int row, int column) throws IOException {
        Train train = getTrainById(trainId);
        if (train == null || !isValidSeat(train, row, column)
                || !Integer.valueOf(1).equals(train.getSeats().get(row).get(column))) {
            return false;
        }

        train.getSeats().get(row).set(column, 0);
        try {
            saveTrains();
        } catch (IOException e) {
            train.getSeats().get(row).set(column, 1);
            throw e;
        }
        return true;
    }

    public boolean servesRoute(String trainId, String source, String destination) {
        Train train = getTrainById(trainId);
        if (train == null || isBlank(source) || isBlank(destination)) {
            return false;
        }
        int sourceIndex = indexOfStation(train, source);
        int destinationIndex = indexOfStation(train, destination);
        return sourceIndex >= 0 && destinationIndex > sourceIndex;
    }

    private boolean hasAvailableSeat(Train train) {
        if (train.getSeats() == null) {
            return false;
        }
        for (List<Integer> row : train.getSeats()) {
            if (row != null && row.contains(0)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAvailableSeat(Train train, int row, int column) {
        return train != null
                && isValidSeat(train, row, column)
                && Integer.valueOf(0).equals(train.getSeats().get(row).get(column));
    }

    private boolean isValidSeat(Train train, int row, int column) {
        return train != null
                && train.getSeats() != null
                && row >= 0
                && row < train.getSeats().size()
                && train.getSeats().get(row) != null
                && column >= 0
                && column < train.getSeats().get(row).size();
    }

    private int indexOfStation(Train train, String station) {
        if (train.getStations() == null) {
            return -1;
        }
        for (int i = 0; i < train.getStations().size(); i++) {
            String currentStation = train.getStations().get(i);
            if (currentStation != null && currentStation.equalsIgnoreCase(station.trim())) {
                return i;
            }
        }
        return -1;
    }

    private void saveTrains() throws IOException {
        objectMapper.writeValue(new File(TRAIN_PATH), trains);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
