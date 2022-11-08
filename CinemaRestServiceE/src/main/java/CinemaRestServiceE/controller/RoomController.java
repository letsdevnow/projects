package CinemaRestServiceE.controller;

import CinemaRestServiceE.common.*;
import CinemaRestServiceE.exception.SeatException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
public class RoomController {
    private final Room room = new Room(3, 3);

    //return json with available seats
    @GetMapping("/seats")
    public RoomWithAvailableSeats getSeats() {
        return room.getRoomWithAvailableSeats();
    }

    //make booking of one available seat, return json with booking response
    @PostMapping("/booking")
    public BookingResponse makeBooking(@RequestBody Seat seat) {
        if (seat.getRow() > room.getTotalRows() || seat.getRow() < 1 ||
                seat.getColumn() > room.getTotalColumns() || seat.getColumn() < 1) {
            throw new SeatException("The number of a row or a column is out of bounds!");
        }

        if (!room.isBooked(seat.getRow(), seat.getColumn())) {
            String token = UUID.randomUUID().toString();
            Seat bookedSeat = room.setBook(seat.getRow(), seat.getColumn(), token);
            Ticket ticket = new Ticket(bookedSeat.getRow(), bookedSeat.getColumn(), bookedSeat.getPrice());
            return new BookingResponse(token, ticket);
        } else {
            throw new SeatException("The ticket has been already purchased!");
        }
    }

    //make refund of booked seat
    @PostMapping("/return")
    public Map<String, Ticket> makeReturn(@RequestBody ReturnRequest returnRequest) {
        if (returnRequest.getToken().equals("")) {
            throw new SeatException("Token is empty!");
        }

        Optional<Seat> optSeat = room.returnTicket(returnRequest.getToken());
        if (optSeat.isPresent()) {
            Map<String, Ticket> wrappedTicket = new HashMap<>();
            wrappedTicket.put("returned_ticket",
                    new Ticket(optSeat.get().getRow(), optSeat.get().getColumn(), optSeat.get().getPrice()));
            return wrappedTicket;
        } else {
            throw new SeatException("Wrong token!");
        }
    }

    //return json with statistics
    @GetMapping("/stats")
    public Statistics getStats() {
        return room.getStatistics();
    }
}
