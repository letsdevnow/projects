package cinemarestservice.common;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;

@Getter
@Setter
public class RoomWithAvailableSeats {
    private int totalRows;
    private int totalColumns;
    private ArrayList<Seat> availableSeats;

    public RoomWithAvailableSeats(int totalRows, int totalColumns) {
        this.totalRows = totalRows;
        this.totalColumns = totalColumns;
    }
}
