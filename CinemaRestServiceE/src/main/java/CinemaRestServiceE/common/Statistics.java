package CinemaRestServiceE.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Statistics {
    private int currentIncome = 0;
    private int numberOfAvailableSeats = 0;
    private int numberOfPurchasedTickets = 0;
}
