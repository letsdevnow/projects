package CinemaRestServiceE.common;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public class Room {
    private int totalRows;
    private int totalColumns;
    private ArrayList<Seat> seats = new ArrayList<>();

    //initialize seats array, fill in cost of seats
    public Room(int totalRows, int totalColumns) {
        this.totalRows = totalRows;
        this.totalColumns = totalColumns;
        seats.ensureCapacity(totalRows * totalColumns);
        for (int i = 0; i < totalRows; i++) {
            int price = i + 1 <= 4 ? 10 : 8;
            for (int j = 0; j < totalColumns; j++) {
                seats.add(new Seat(i + 1, j + 1, price));
            }
        }
    }

    //check whether the seat is booked
    public boolean isBooked(int row, int column) {
        return seats.get((row - 1) * totalColumns + column - 1).isBooked();
    }

    //save booking in seats array
    public Seat setBook(int row, int column, String token) {
        int seatID = (row - 1) * totalColumns + column - 1;
        seats.get(seatID).setBooked(true);
        seats.get(seatID).setToken(token);
        return new Seat(row, column, seats.get(seatID).getPrice());
    }

    //return information of available seats within RoomWithAvailableSeats instance
    public RoomWithAvailableSeats getRoomWithAvailableSeats() {
        RoomWithAvailableSeats roomWithAvailableSeats = new RoomWithAvailableSeats(totalRows, totalColumns);
        roomWithAvailableSeats.setAvailableSeats(seats.stream()
                .filter(seat -> !seat.isBooked())
                .collect(Collectors.toCollection(ArrayList::new))
        );
        return roomWithAvailableSeats;
    }

    //make refund of the seat in seats array
    public Optional<Seat> returnTicket(String token) {
        Optional<Seat> optSeat = seats.stream().filter(s -> s.getToken().equals(token)).findFirst();
        if (optSeat.isPresent()) {
            optSeat.get().setToken("");
            optSeat.get().setBooked(false);
        }
        return optSeat;
    }

    //provide statistics about seats (sum on booked seats, number of available seats, number of booked seats)
    public Statistics getStatistics() {
        Statistics statistics = new Statistics();
        statistics.setCurrentIncome(seats.stream()
                .filter(Seat::isBooked)
                .mapToInt(Seat::getPrice)
                .sum()
        );
        statistics.setNumberOfAvailableSeats((int) seats.stream()
                .filter(s -> !s.isBooked())
                .count()
        );
        statistics.setNumberOfPurchasedTickets((int) seats.stream()
                .filter(Seat::isBooked)
                .count()
        );
        return statistics;
    }
}
